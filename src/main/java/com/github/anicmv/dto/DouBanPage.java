package com.github.anicmv.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

/**
 * @author anicmv
 * @date 2025/3/23 17:28
 * @description 豆瓣页面对象
 */
@Data
@Builder
public class DouBanPage {
    private String context;
    private String name;
    private String imdb;
    private String genres;
    private String tags;
    private String season;
    private String url;
    private String image;
    private String languages;
    private Integer episodesCount;
    private String officialWebsite;
    private String firstBroadcast;
    private String publishDate;
    private String countries;
    private List<Person> director;
    private List<Person> author;
    private List<Person> actor;
    // 格式：YYYY-MM-DD
    private String datePublished;
    private List<String> genre;
    // ISO 8601 时间段格式，例如 PT0H24M
    private String duration;
    private String description;
    private String type;
    private AggregateRating aggregateRating;
}

@Data
@Builder
class AggregateRating {
    private String type;
    private int ratingCount;
    private int bestRating;
    private int worstRating;
    private double ratingValue;
}
