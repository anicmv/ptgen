package com.github.anicmv.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.config.DouBanConfig;
import com.github.anicmv.constant.PtGenConstant;
import com.github.anicmv.dto.*;
import com.github.anicmv.entity.DouBan;
import com.github.anicmv.mapper.DouBanMapper;
import com.github.anicmv.service.PtGenService;
import com.github.anicmv.util.HttpUtil;
import com.github.anicmv.util.PtGenUtil;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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
    private final ConcurrentHashMap<Integer, ReentrantLock> locks = new ConcurrentHashMap<>();

    /**
     * 分段锁获取ptGen
     *
     * @param douBanId 豆瓣id
     */
    @Override
    public ResponseEntity<String> ptGen(Integer douBanId) {
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
            } else if (PtGenUtil.hasPast30Days(douBan.getUpdateTime())) {
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
    public ResponseEntity<JSONObject> detail(Integer douBanId) {
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
    public ResponseEntity<String> oldData(Integer douBanId) {
        DouBan douBan = mapper.selectById(douBanId);
        if (douBan == null) {
            log.error("数据库无数据! douBanId: {}", douBanId);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(douBan.buildPtGen());
    }

    /**
     * 缓存ptgen信息
     *
     * @param douBan 豆瓣
     * @return ok
     */
    @Override
    public ResponseEntity<JSONObject> save(DouBan douBan) {
        if (douBan.getId() == null) {
            return PtGenUtil.error("豆瓣: null");
        }
        mapper.insertOrUpdate(douBan);
        return PtGenUtil.success();
    }


    /**
     * 获取存量豆瓣
     *
     * @param douBanId 豆瓣id
     */
    @Override
    public ResponseEntity<JSONObject> cache(Integer douBanId) {
        DouBan douBan = mapper.selectById(douBanId);
        if (douBan == null) {
            return PtGenUtil.error("豆瓣: " + douBanId + ", not exist");
        }
        if (PtGenUtil.hasPast30Days(douBan.getUpdateTime())) {
            return PtGenUtil.error("豆瓣: " + douBanId + ", expired");
        }
        JSONObject douBanJson = JSONUtil.parseObj(douBan);
        if (douBanJson == null) {
            return PtGenUtil.error("豆瓣: " + douBanId + ", convert error");
        }
        douBanJson.set("createTime", douBan.getCreateTime().getTime());
        douBanJson.set("updateTime", douBan.getUpdateTime().getTime());
        return PtGenUtil.success(douBanJson);
    }

    @Override
    public ResponseEntity<String> searchByKeyword(String keyword) {
        String api = config.getSearchApi() + keyword;
        String result = HttpUtil.get(api, Map.of(PtGenConstant.COOKIE, config.getCookie(), PtGenConstant.UA, PtGenConstant.USER_AGENT));
        if (result == null) {
            return null;
        }
        return ResponseEntity.ok().body(JSONUtil.parseArray(result).toStringPretty());
    }


    /**
     * 获取返回数据
     *
     * @param douBanId 豆瓣id
     * @param exist    是否存在
     */
    private ResponseData getData(Integer douBanId, Boolean exist) {
        DouBanDetail detail = getMovieDetail(douBanId);
        if (detail == null) {
            return null;
        }

        DouBan douBan = getDouBan(douBanId, detail);
        updateDouBan(douBan, exist);

        return ResponseData.builder().douBan(douBan).douBanDetail(detail).build().buildPtGen();
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
     * 获取api数据。页面爬取已移除，无法再从页面判断 movie/tv，故先按 movie 请求，失败再按 tv 请求
     *
     * @param douBanId 豆瓣id
     */
    private DouBanDetail getMovieDetail(Integer douBanId) {
        DouBanDetail detail = fetchDetail(douBanId, "movie");
        if (detail == null || StrUtil.isEmpty(detail.getTitle())) {
            log.info("movie 接口未获取到数据，尝试 tv 接口 douBanId: {}", douBanId);
            detail = fetchDetail(douBanId, "tv");
        }
        return detail;
    }

    private DouBanDetail fetchDetail(Integer douBanId, String type) {
        String api = config.getDetailApi() + type + PtGenConstant.S + douBanId + PtGenConstant.API_KEY + config.getApikey();
        String result = HttpUtil.get(api, Map.of(PtGenConstant.UA, config.getUserAgent(), PtGenConstant.REFERER, config.getReferer()));
        if (result == null) {
            return null;
        }
        JSONObject detailJson = JSONUtil.parseObj(result);
        return JSONUtil.toBean(detailJson, DouBanDetail.class);
    }

    /**
     * 构建豆瓣对象
     *
     * @param douBanId 豆瓣id
     * @param detail   豆瓣api数据
     * @return 豆瓣
     */
    private DouBan getDouBan(Integer douBanId, DouBanDetail detail) {
        String akaStr = detail.getAka() == null ? "" : String.join(" / ", detail.getAka());
        String originalTitle = StrUtil.isEmpty(detail.getOriginalTitle()) ? detail.getTitle() : detail.getOriginalTitle();
        String translatedName = StrUtil.isEmpty(detail.getOriginalTitle()) ? akaStr : detail.getTitle() + " / " + akaStr;

        return DouBan.builder()
                .id(douBanId)
                .title(detail.getTitle())
                .type("tv".equalsIgnoreCase(detail.getType()) ? "TVSeries" : "Movie")
                .originalTitle(originalTitle)
                .translatedName(translatedName)
                .year(Integer.parseInt(detail.getYear()))
                .countries(join(detail.getCountries()))
                .mainPic(detail.getPic() == null ? null : detail.getPic().getLarge())
                .genres(join(detail.getGenres()))
                .languages(join(detail.getLanguages()))
                .publishDate(join(detail.getPubdate()))
                .douBanRating(detail.getRating() == null ? null : BigDecimal.valueOf(detail.getRating().getValue()))
                .douBanRatingCount(detail.getRating() == null ? null : detail.getRating().getCount())
                .episodesCount(detail.getEpisodesCount() > 0 ? detail.getEpisodesCount() : null)
                .durations(join(detail.getDurations()))
                .directors(joinNames(detail.getDirectors()))
                .actors(joinNames(detail.getActors(), "\n" + "　　　　　 "))
                .intro(detail.getIntro())
                .build();
    }

    private static String join(List<String> list) {
        return list == null || list.isEmpty() ? null : String.join(" / ", list);
    }

    private static String joinNames(List<DouBanDetail.SimpleName> list) {
        return joinNames(list, " / ");
    }

    private static String joinNames(List<DouBanDetail.SimpleName> list, String delimiter) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.stream().map(DouBanDetail.SimpleName::getName).collect(Collectors.joining(delimiter));
    }
}
