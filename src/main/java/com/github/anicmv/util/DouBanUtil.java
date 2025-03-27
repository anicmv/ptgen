package com.github.anicmv.util;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.anicmv.enums.PtGenEnum;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author anicmv
 * @date 2025/3/24 20:22
 * @description 豆瓣工具类
 */
public class DouBanUtil {

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
     * @param startDateTime 数据库更新日期
     * @return true 过期
     */
    public static boolean hasPast30Days(LocalDateTime startDateTime) {
        LocalDateTime thirtyDaysLater = startDateTime.plusDays(30);
        return thirtyDaysLater.isBefore(LocalDateTime.now());
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
}
