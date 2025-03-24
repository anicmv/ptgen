package com.github.anicmv.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author anicmv
 * @date 2025/3/22 19:56
 * @description 豆瓣配置
 */
@Setter
@Getter
@Configuration
@ConfigurationProperties("douban")
public class DouBanConfig {
    private String detailApi;
    private String apikey;
    private String accessToken;
    private String role;
    private String userAgent;
    private String referer;
    private String cookie;
}