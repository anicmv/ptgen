package com.github.anicmv.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.config.DouBanConfig;
import com.github.anicmv.constant.DouBanConstant;
import com.github.anicmv.dto.DouBanDetail;
import com.github.anicmv.dto.DouBanPage;
import com.github.anicmv.dto.Person;
import com.github.anicmv.dto.ResponseData;
import com.github.anicmv.entity.DouBan;
import com.github.anicmv.mapper.DouBanMapper;
import com.github.anicmv.service.PtGenService;
import com.github.anicmv.util.DouBanUtil;
import com.github.anicmv.util.HttpUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

/**
 * @author anicmv
 * @date 2025/3/22 19:54
 * @description DouBan服务类 todo 如果异常 则丢出老数据
 */
@Slf4j
@Service
public class PtGenServiceImpl implements PtGenService {

    @Resource
    private DouBanConfig config;

    @Resource
    private DouBanMapper mapper;

    // 存放每个 douBanId 对应的锁
    private final ConcurrentHashMap<String, ReentrantLock> locks = new ConcurrentHashMap<>();

    /**
     * 分段锁获取ptGen
     *
     * @param douBanId 豆瓣id
     */
    @Override
    public ResponseEntity<String> ptGen(String douBanId) {
        // 获取对应锁
        ReentrantLock lock = locks.computeIfAbsent(douBanId, k -> new ReentrantLock());
        lock.lock();
        try {
            DouBan douBan = mapper.selectById(douBanId);
            String ptGen;
            if (douBan == null) {
                log.info("没有数据，调取接口获取新数据 douBanId: {}", douBanId);
                ResponseData data = getData(douBanId, false);
                ptGen = (data != null && data.getPtGen() != null) ? data.getPtGen() : "";
            } else if (DouBanUtil.hasPast30Days(douBan.getUpdateTime())) {
                log.info("数据已过期，重新请求数据 douBanId: {}", douBanId);
                ResponseData data = getData(douBanId, true);
                ptGen = (data != null && data.getPtGen() != null) ? data.getPtGen() : "";
            } else {
                log.info("数据库存量数据构造结果 douBanId: {}", douBanId);
                ptGen = douBan.buildPtGen();
            }
            return ResponseEntity.ok(ptGen);
        } catch (Exception e) {
            log.error("处理 douBanId: {} 时发生异常", douBanId, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("内部错误");
        } finally {
            lock.unlock();
            // 加锁保护，防止并发环境下误删正在使用的锁
            locks.compute(douBanId, (key, currentLock) -> {
                if (currentLock != null && !currentLock.isLocked() && !currentLock.hasQueuedThreads()) {
                    return null;
                }
                return currentLock;
            });
        }
    }

    /**
     * 获取所有信息
     *
     * @param douBanId 豆瓣id
     */
    @Override
    public ResponseEntity<JSONObject> detail(String douBanId) {
        ResponseData data = getData(douBanId, null);
        return ResponseEntity.ok(JSONUtil.parseObj(data));
    }


    /**
     * 直接获取数据库数据
     *
     * @param douBanId 豆瓣id
     * @return ptGen
     */
    @Override
    public ResponseEntity<String> oldData(String douBanId) {
        DouBan douBan = mapper.selectById(douBanId);
        if (douBan == null) {
            log.error("数据库无数据! douBanId: {}", douBanId);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(douBan.buildPtGen());
    }


    /**
     * 获取返回数据
     *
     * @param douBanId 豆瓣id
     * @param exist    是否存在
     */
    private ResponseData getData(String douBanId, Boolean exist) {
        DouBanPage douBanPage = getPageObj(douBanId);
        if (douBanPage == null) {
            return null;
        }

        String type = "TVSeries".equals(douBanPage.getType()) ? "tv" : "movie";
        // 信息接口
        DouBanDetail detail = getMovieDetail(douBanId, type);

        if (detail == null) {
            return null;
        }

        // 拿获奖名单
        Map<String, List<String>> awards = getAwards(douBanId);

        // 豆瓣信息
        DouBan douBan = getDouBan(douBanId, douBanPage, detail, awards);
        updateDouBan(douBan, exist);

        return ResponseData.builder().douBan(douBan).douBanDetail(detail).douBanPage(douBanPage).build().buildPtGen();
    }


    /**
     * 更新豆瓣信息
     *
     * @param douBan 豆瓣
     * @param exist  是否存在
     */
    private void updateDouBan(DouBan douBan, Boolean exist) {
        boolean shouldUpdate = (exist == null) ? (mapper.selectById(douBan.getId()) != null) : exist;
        if (shouldUpdate) {
            mapper.updateById(douBan);
        } else {
            mapper.insert(douBan);
        }
    }

    /**
     * 获取 豆瓣页面数据
     *
     * @param douBanId 豆瓣id
     */
    private DouBanPage getPageObj(String douBanId) {
        String douBanLink = DouBanConstant.D_LINK + douBanId + DouBanConstant.S;
        String pageRaw = HttpUtil.get(douBanLink, Map.of(DouBanConstant.COOKIE, config.getCookie(), DouBanConstant.UA, DouBanConstant.USER_AGENT));

        if (checkPageError(pageRaw)) {
            return null;
        }

        return getPageScriptJson(pageRaw, douBanLink);
    }

    /**
     * 获取api数据
     *
     * @param douBanId 豆瓣info
     * @param type     movie or tv
     */
    private DouBanDetail getMovieDetail(String douBanId, String type) {
        String api = config.getDetailApi() + type + DouBanConstant.S + douBanId + DouBanConstant.API_KEY + config.getApikey();
        String result = HttpUtil.get(api, Map.of(DouBanConstant.UA, config.getUserAgent(), DouBanConstant.REFERER, config.getReferer()));
        if (result == null) {
            return null;
        }
        JSONObject detailJson = JSONUtil.parseObj(result);
        return JSONUtil.toBean(detailJson, DouBanDetail.class);
    }

    /**
     * 构建豆瓣对象
     *
     * @param douBanId   豆瓣id
     * @param douBanPage 豆瓣页面数据
     * @param detail     豆瓣api数据
     * @param awards     奖杯页面信息
     * @return 豆瓣
     */
    private DouBan getDouBan(String douBanId, DouBanPage douBanPage, DouBanDetail detail, Map<String, List<String>> awards) {
        // 构建豆瓣对象
        return DouBan.builder().id(douBanId).title(detail.getTitle()).type(douBanPage.getType()).originalTitle(detail.getOriginalTitle()).translatedName(detail.getTitle() + " / " + String.join(" / ", detail.getAka())).year(Integer.parseInt(detail.getYear())).countries(douBanPage.getCountries()).officialWebsite(douBanPage.getOfficialWebsite()).mainPic(detail.getPic().getLarge()).genres(douBanPage.getGenres()).languages(douBanPage.getLanguages()).publishDate(String.join(" / ", detail.getPubdate())).douBanScore(BigDecimal.valueOf(detail.getRating().getValue())).douBanPeople(String.valueOf(detail.getRating().getCount())).imdbId(douBanPage.getImdb()).season(douBanPage.getSeason()).episodesCount(douBanPage.getEpisodesCount()).durations(douBanPage.getDuration()).directors(douBanPage.getDirector().stream().map(Person::getName).collect(Collectors.joining(" / "))).actors(douBanPage.getActor().stream().map(Person::getName).collect(Collectors.joining("\n" + "　　　"))).dramatist(douBanPage.getAuthor().stream().map(Person::getName).collect(Collectors.joining(" / "))).tags(douBanPage.getTags()).intro(detail.getIntro()).awards(JSONUtil.parseObj(awards).toString()).createTime(LocalDateTime.now()).updateTime(LocalDateTime.now()).build();
    }


    /**
     * 获取奖杯信息页面数据
     *
     * @param douBanId 豆瓣id
     */
    private Map<String, List<String>> getAwards(String douBanId) {
        String awardsPageUrl = DouBanConstant.D_LINK + douBanId + DouBanConstant.AWARDS;
        String awardsPage = HttpUtil.get(awardsPageUrl, Map.of(DouBanConstant.COOKIE, config.getCookie()));
        if (awardsPage == null) {
            return null;
        }
        Document awardsDoc = Jsoup.parse(awardsPage, awardsPage);
        Element awardsContent = awardsDoc.selectFirst("#content > div > div.article");
        if (awardsContent == null) {
            return null;
        }
        Elements awardsEle = awardsContent.select(".awards");

        Map<String, List<String>> awardsMap = new LinkedHashMap<>();

        awardsEle.forEach(awardEle -> {
            Element h2 = awardEle.selectFirst("h2");
            List<String> awards = new ArrayList<>();
            if (h2 != null) {
                awardsMap.put(h2.text(), awards);
            }
            Elements ae = awardEle.select(".award");
            ae.forEach(a -> awards.add(a.text()));
        });
        return awardsMap;
    }


    /**
     * 检查错误页面
     *
     * @param pageRaw html
     * @return true表示爬取错误
     */
    private boolean checkPageError(String pageRaw) {
        if (StrUtil.isEmpty(pageRaw)) {
            log.error("empty page");
            return true;
        }
        if (pageRaw.contains("你想访问的页面不存在")) {
            log.error("NONE_EXIST_ERROR");
            return true;
        } else if (pageRaw.contains("检测到有异常请求")) {
            log.error("GenHelp was temporary banned by Douban, Please wait....");
            return true;
        }
        return false;
    }

    /**
     * 获取page对象
     *
     * @param pageRaw    html
     * @param douBanLink 豆瓣链接
     * @return page
     */
    private DouBanPage getPageScriptJson(String pageRaw, String douBanLink) {
        Document doc = Jsoup.parse(pageRaw, douBanLink);
        Element ldElement = doc.selectFirst("head > script[type=application/ld+json]");
        JSONObject pageJson = new JSONObject();
        if (ldElement != null) {
            String ldContent = ldElement.html().replaceAll("(\\r\\n|\\n|\\r|\\t)", "").replaceAll("@type", "type").replaceAll("@context", "context");
            pageJson = new JSONObject(ldContent);
        }

        Map<String, String> infoMap = fetchInfoMap(doc);
        if (infoMap != null) {
            // imdb
            pageJson.putOpt("imdb", infoMap.get("IMDb"));
            // 季度
            pageJson.putOpt("season", infoMap.get("season"));
            // 类型
            pageJson.putOpt("genres", infoMap.get("genres"));
            // 上映日期
            pageJson.putOpt("publishDate", infoMap.get("publishDate"));
            // 官方网站
            pageJson.putOpt("officialWebsite", infoMap.get("officialWebsite"));
            // 集数
            pageJson.putOpt("episodesCount", infoMap.get("episodesCount"));
            // 单集片长
            pageJson.putOpt("durations", infoMap.get("durations"));
            // 首播
            pageJson.putOpt("firstBroadcast", infoMap.get("firstBroadcast"));
            // 制片国家/地区/产地
            pageJson.putOpt("countries", infoMap.get("countries"));
            // 语言
            pageJson.putOpt("languages", infoMap.get("languages"));
        }


        // 标签
        Elements tagEls = doc.select("div.tags-body > a[href^=/tag]");
        if (!tagEls.isEmpty()) {
            List<String> tagList = tagEls.eachText();
            pageJson.putOpt("tags", new JSONArray(tagList));
        }

        return JSONUtil.toBean(pageJson, DouBanPage.class);
    }

    /**
     * 获取 #info元素信息
     *
     * @param doc DouBan页面对象
     */
    private Map<String, String> fetchInfoMap(Document doc) {
        Element infoEle = doc.select("#info").first();
        if (infoEle == null) {
            return null;
        }

        String info = infoEle.wholeText().replaceAll("\n\n", "\n");
        String[] infos = info.split("\n");

        Map<String, String> infoMap = new HashMap<>();
        for (String str : infos) {
            getInfoMap(str.trim().split(": "), infoMap);
        }
        return infoMap;
    }

    private static void getInfoMap(String[] partInfo, Map<String, String> infoMap) {
        if (ObjectUtil.isEmpty(partInfo)) {
            return;
        }

        switch (partInfo[0]) {
            case "导演":
                infoMap.put("directors", partInfo[1]);
                break;
            case "编剧":
                infoMap.put("dramatist", partInfo[1]);
                break;
            case "主演":
                infoMap.put("actors", partInfo[1]);
                break;
            case "类型":
                infoMap.put("genres", partInfo[1]);
                break;
            case "官方网站":
                infoMap.put("officialWebsite", partInfo[1]);
                break;
            case "制片国家/地区":
                infoMap.put("countries", partInfo[1]);
                break;
            case "语言":
                infoMap.put("languages", partInfo[1]);
                break;
            case "首播":
                infoMap.put("firstBroadcast", partInfo[1]);
                break;
            case "上映日期":
                infoMap.put("publishDate", partInfo[1]);
                break;
            case "季度":
                infoMap.put("season", partInfo[1]);
                break;
            case "集数":
                infoMap.put("episodesCount", partInfo[1]);
                break;
            case "单集片长", "片长":
                infoMap.put("durations", partInfo[1]);
                break;
            case "又名":
                infoMap.put("otherName", partInfo[1]);
                break;
            case "IMDb":
                infoMap.put("IMDb", partInfo[1]);
                break;
        }
    }


}
