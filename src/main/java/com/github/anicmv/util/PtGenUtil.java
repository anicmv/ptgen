package com.github.anicmv.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.entity.DouBan;
import com.github.anicmv.enums.PtGenEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author anicmv
 * @date 2025/3/24 20:22
 * @description 豆瓣工具类
 */
@Slf4j
public class PtGenUtil {

    /**
     * 提取 URL 中的豆瓣 ID
     *
     * @param url 请求参数 URL
     * @return 豆瓣 ID
     * @throws IllegalArgumentException 当 URL 无法匹配时抛出
     */
    public static Integer extractDouBanId(String url) {
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher matcher = pattern.matcher(url);
        if (!matcher.find()) {
            throw new IllegalArgumentException("Invalid URL, can't extract DouBan ID");
        }
        return Integer.valueOf(matcher.group(1));
    }


    /**
     * 30天过期判断
     *
     * @param startDate 数据库更新日期
     * @return true 表示已过期（即超过30天）
     */
    public static boolean hasPast30Days(Date startDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        Date thirtyDaysLater = calendar.getTime();

        // 当前时间
        Date now = new Date();
        return thirtyDaysLater.before(now);
    }


    /**
     * 30天过期判断
     *
     * @param startDateTime 数据库更新日期
     * @return true 过期
     */
    public static boolean hasPast30Days(LocalDateTime startDateTime) {
        LocalDateTime thirtyDaysLater = startDateTime.plusDays(30);
        return thirtyDaysLater.isBefore(LocalDateTime.now());
    }


    /**
     * 30天过期判断
     *
     * @param startTime 数据库更新日期
     * @return true 表示已过期（即超过30天）
     */
    public static boolean hasPast30Days(Timestamp startTime) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startTime);
        calendar.add(Calendar.DAY_OF_YEAR, 30);
        Timestamp thirtyDaysLater = new Timestamp(calendar.getTimeInMillis());

        // 当前时间
        Timestamp now = new Timestamp(System.currentTimeMillis());
        return thirtyDaysLater.before(now);
    }


    public static ResponseEntity<JSONObject> success() {
        JSONObject result = JSONUtil.createObj()
                .putOpt("result", PtGenEnum.SUCCESS.getResult())
                .putOnce("code", PtGenEnum.SUCCESS.getCode())
                .putOnce("data", "ok");
        return ResponseEntity.ok().body(result);
    }


    public static ResponseEntity<JSONObject> success(Object data) {
        JSONObject result = JSONUtil.createObj()
                .putOpt("result", PtGenEnum.SUCCESS.getResult())
                .putOnce("code", PtGenEnum.SUCCESS.getCode())
                .putOnce("data", data);
        return ResponseEntity.ok().body(result);
    }


    public static ResponseEntity<JSONObject> error(String msg) {
        JSONObject result = JSONUtil.createObj()
                .putOpt("result", PtGenEnum.FAILURE.getResult())
                .putOnce("code", PtGenEnum.FAILURE.getCode())
                .putOnce("data", msg);
        return ResponseEntity.ok().body(result);
    }


    /**
     * 解析 JSONP 返回，提取内部的 JSON 数据
     *
     * @param responseText JSONP 格式字符串，例如 foo({...});
     * @return 一个 JSONObject 对象，如果解析出错则返回空 JSONObject
     */
    public static JSONObject jsonpParser(String responseText) {
        try {
            // 去掉换行符
            String cleaned = responseText.replaceAll("\\n", "");
            // 正则表达式，匹配形如 '函数名({...})' 的格式
            Pattern pattern = Pattern.compile("[^(]+\\((.+)\\)");
            Matcher matcher = pattern.matcher(cleaned);
            if (matcher.find()) {
                String jsonString = matcher.group(1);
                return new JSONObject(jsonString);
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return new JSONObject();
    }


    /**
     * 获取时长
     *
     * @param durations 时长 PT0H24M
     * @return 整数
     */
    public static int getDurations(String durations) {
        if (StrUtil.isEmpty(durations)) {
            return 0;
        }
        Duration duration = Duration.parse(durations);
        return (int) duration.toMinutes();
    }



    /**
     * 将获奖信息通过空行分组转换为 JSON 对象。
     * 每组第一行为奖项标题，其余非空行为详情。
     *
     * @param douBan awards多行文本格式的获奖信息
     */
    public static void convertAwards(DouBan douBan) {
        String awardsRaw = douBan.getAwards();
        if (StrUtil.isEmpty(awardsRaw)) {
            return;
        }
        // 使用 LinkedHashMap 保持顺序
        Map<String, JSONArray> awardsMap = new LinkedHashMap<>();
        // 先按照换行符拆分每一行，支持 Windows 与 Unix 行尾
        String[] lines = awardsRaw.split("\\r?\\n");
        String currentTitle = null;
        JSONArray currentDetails = null;

        for (String line : lines) {
            // 去除前后空白及全角空格
            line = line.trim().replaceAll("\\u3000", "");
            if(line.isEmpty()){
                // 遇到空行时，重置当前组，使下一行作为新的标题处理
                currentTitle = null;
                currentDetails = null;
                continue;
            }
            if(currentTitle == null){
                // 当前组，第一行视为标题
                currentTitle = line;
                currentDetails = new JSONArray();
                awardsMap.put(currentTitle, currentDetails);
            } else {
                // 当前组后续行视为详情
                currentDetails.put(line);
            }
        }

        // 构造 JSONObject
        JSONObject result = new JSONObject();
        result.putAll(awardsMap);
        douBan.setAwards(result.toString());
    }
}
