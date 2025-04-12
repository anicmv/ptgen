package com.github.anicmv.util;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.github.anicmv.constant.PtGenConstant;
import lombok.extern.log4j.Log4j2;

import java.io.*;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Map;
import java.util.zip.GZIPInputStream;

/**
 * @author anicmv
 * @date 2025/3/22 22:46
 * @description 工具类
 */
@Log4j2
public class HttpUtil {

    private static final HttpClient CLIENT = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    public static HttpClient getHttpClient() {
        return CLIENT;
    }

    public static HttpRequest.Builder requestBuilderGet(String url, Map<String, String> headers) {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10));
        if (ObjectUtil.isNotEmpty(headers)) {
            headers.forEach(requestBuilder::header);
        }
        return requestBuilder;
    }


    public static String get(String url, Map<String, String> headers) {
        if (StrUtil.isEmpty(url)) {
            log.error("URL cannot be null or empty");
            return null;
        }

        try {
            HttpRequest request = requestBuilderGet(url, headers).build();
            HttpResponse<String> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();

            if (statusCode < 200 || statusCode >= 300) {
                log.error("Request to {} failed with status code: {}", url, statusCode);
                return null;
            }

            return response.body();
        } catch (Exception e) {
            log.error("Error occurred during GET request to {}: ", url, e);
        }
        return null;
    }

    public static String getImdbJson(String imdbApi, Map<String, String> headers) {
        HttpRequest request = requestBuilderGet(imdbApi, headers).build();
        String imdbApiRaw = "";
        try {
            HttpResponse<byte[]> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofByteArray());
            byte[] body = response.body();
            String encoding = response.headers().firstValue(PtGenConstant.CE).orElse("");

            if ("gzip".equalsIgnoreCase(encoding)) {
                imdbApiRaw = decompressGzip(body);
            } else {
                imdbApiRaw = new String(body, StandardCharsets.UTF_8);
            }
        } catch (IOException | InterruptedException e) {
            log.error("Error occurred during GET request to {}: ", imdbApi, e);
        }
        return imdbApiRaw;
    }

    private static String decompressGzip(byte[] compressedData) throws IOException {
        try (GZIPInputStream gis = new GZIPInputStream(new ByteArrayInputStream(compressedData));
             InputStreamReader reader = new InputStreamReader(gis, StandardCharsets.UTF_8);
             BufferedReader in = new BufferedReader(reader)) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                sb.append(line);
            }
            return sb.toString();
        }
    }

    public static InputStream getInputStream(String url, Map<String, String> headers) {
        if (StrUtil.isEmpty(url)) {
            log.error("URL cannot be null or empty");
            return null;
        }

        try {
            HttpRequest request = requestBuilderGet(url, headers).build();
            HttpResponse<InputStream> response = getHttpClient().send(request, HttpResponse.BodyHandlers.ofInputStream());
            int statusCode = response.statusCode();

            if (statusCode < 200 || statusCode >= 300) {
                log.error("Request to {} failed with status code: {}", url, statusCode);
                return null;
            }

            return response.body();
        } catch (Exception e) {
            log.error("Error occurred during GET request to {}: ", url, e);
        }
        return null;
    }

}
