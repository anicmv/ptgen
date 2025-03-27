package com.github.anicmv.entity;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.github.anicmv.constant.DouBanConstant;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author anicmv
 * @date 2025/3/23 16:58
 * @description 豆瓣对象
 */
@Data
@Builder
public class DouBan {
    // 豆瓣id
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    // 标题
    private String title;
    // 豆瓣类型 TVSeries or Movie
    private String type;
    // 片名
    private String originalTitle;
    // 译名
    private String translatedName;
    // 年代
    private int year;
    // 制片国家/地区/产地
    private String countries;
    // 官方网站
    private String officialWebsite;
    // 海报链接
    private String mainPic;
    // 类型/类别
    private String genres;
    // 语言
    private String languages;
    // 首播/上映日期
    private String publishDate;
    // 豆瓣评分
    private BigDecimal douBanScore;
    // 豆瓣评分人数
    private String douBanPeople;
    // IMDb
    private String imdbId;
    // 季度
    private String season;
    // 集数
    private Integer episodesCount;
    // 单集片长
    private String durations;
    // 导演
    private String directors;
    // 主演/演员
    private String actors;
    // 编剧
    private String dramatist;
    // 标签
    private String tags;
    // 简介
    private String intro;
    // 获奖情况
    private String awards;
    // 创建时间
    private LocalDateTime createTime;
    // 更新时间
    private LocalDateTime updateTime;

    // 构建ptGen
    public String buildPtGen() {
        StringBuilder ptGen = new StringBuilder();
        if (StrUtil.isNotEmpty(mainPic)) {
            ptGen.append("[img]").append(mainPic).append("[/img]\n\n");
        }
        if (StrUtil.isNotEmpty(translatedName)) {
            ptGen.append("◎译　　名　").append(translatedName).append("\n");
        }
        if (StrUtil.isNotEmpty(originalTitle)) {
            ptGen.append("◎片　　名　").append(originalTitle).append("\n");
        }
        if (ObjectUtil.isNotEmpty(year)) {
            ptGen.append("◎年　　代　").append(year).append("\n");
        }
        if (StrUtil.isNotEmpty(countries)) {
            ptGen.append("◎产　　地　").append(countries).append("\n");
        }
        if (StrUtil.isNotEmpty(genres)) {
            ptGen.append("◎类　　型　").append(genres).append("\n");
        }
        if (StrUtil.isNotEmpty(officialWebsite)) {
            ptGen.append("◎官方网站　").append(officialWebsite).append("\n");
        }
        if (StrUtil.isNotEmpty(languages)) {
            ptGen.append("◎语　　言　").append(languages).append("\n");
        }
        if (StrUtil.isNotEmpty(publishDate)) {
            ptGen.append("◎上映日期　").append(publishDate).append("\n");
        }
        //if (data.has("imdb_rating")) {
        //     ptGenInfo.append("◎IMDb评分  ").append(data.getString("imdb_rating")).append("\n");
        //}
        //if (!imdbLink.isEmpty()) {
        //     ptGenInfo.append("◎IMDb链接  ").append(imdbLink).append("\n");
        //}
        if (ObjectUtil.isNotEmpty(douBanScore)) {
            //◎豆瓣评分　7.6/10 from 338581 users
            ptGen.append("◎豆瓣评分　").append(douBanScore);
            if (douBanPeople != null && douBanScore.compareTo(BigDecimal.ZERO) > 0) {
                ptGen.append(" from ").append(douBanPeople).append(" users");
            }
            ptGen.append("\n");
        }
        ptGen.append("◎豆瓣链接　").append(DouBanConstant.D_LINK).append(id).append("/").append("\n");
        if (StrUtil.isNotEmpty(season)) {
            ptGen.append("◎季　　度　").append(season).append("\n");
        }
        if (ObjectUtil.isNotEmpty(episodesCount)) {
            ptGen.append("◎集　　数　").append(episodesCount).append("\n");
        }
        if (StrUtil.isNotEmpty(durations)) {
            Duration duration = Duration.parse(durations);
            long minutes = duration.toMinutes();
            ptGen.append("◎片　　长　").append(minutes).append("分钟").append("\n");
        }
        if (StrUtil.isNotEmpty(directors)) {
            ptGen.append("◎导　　演　").append(directors).append("\n");
        }
        if (StrUtil.isNotEmpty(dramatist)) {
            ptGen.append("◎编　　剧　").append(dramatist).append("\n");
        }
        if (StrUtil.isNotEmpty(actors)) {
            ptGen.append("◎主　　演　").append(actors).append("\n");
        }
        if (StrUtil.isNotEmpty(tags)) {
            ptGen.append("◎标　　签　").append(tags).append("\n");
        }
        if (StrUtil.isNotEmpty(intro)) {
            ptGen.append("\n◎简　　介\n\n　　").append(intro.replaceAll("\n", "\n　　")).append("\n");
        }
        if (awards != null && awards.isEmpty()) {
            JSONObject awards = JSONUtil.parseObj(actors);
            if (ObjectUtil.isNotEmpty(awards)) {
                StringBuilder awardsStr = new StringBuilder();
                awards.forEach((key, value) -> {
                    awardsStr.append(key).append("\n");
                    JSONUtil.parseArray(value).forEach(v -> awardsStr.append(v).append("\n"));
                    awardsStr.append("\n");
                });
                ptGen.append("\n◎获奖情况\n\n　　").append(awardsStr.toString().replaceAll("\n", "\n　　")).append("\n");
            }
        }
        return ptGen.toString();
    }
}