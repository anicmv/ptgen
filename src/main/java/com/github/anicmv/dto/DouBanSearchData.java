package com.github.anicmv.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author anicmv
 * @date 2025/10/4 16:12
 * @description 豆瓣搜索结果
 */
@Data
@Builder
public class DouBanSearchData {
    private String id;
    private String episode;
    private String img;
    private String title;
    private String url;
    private String type;
    private String year;
    private String subTitle;
}
