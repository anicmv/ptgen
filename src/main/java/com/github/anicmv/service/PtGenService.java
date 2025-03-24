package com.github.anicmv.service;

import cn.hutool.json.JSONObject;
import org.springframework.http.ResponseEntity;

/**
 * @author anicmv
 * @date 2025/3/24 20:21
 * @description 豆瓣接口
 */
public interface PtGenService {

    ResponseEntity<String> ptGen(String douBanId);

    ResponseEntity<JSONObject> detail(String douBanId);

    ResponseEntity<String> oldData(String douBanId);
}
