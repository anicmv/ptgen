package com.github.anicmv.service;

import cn.hutool.json.JSONObject;
import com.github.anicmv.entity.DouBan;
import org.springframework.http.ResponseEntity;

/**
 * @author anicmv
 * @date 2025/3/24 20:21
 * @description 豆瓣接口
 */
public interface PtGenService {

    ResponseEntity<String> ptGen(Integer douBanId);

    ResponseEntity<JSONObject> detail(Integer douBanId);

    ResponseEntity<String> oldData(Integer douBanId);

    ResponseEntity<JSONObject> save(DouBan douBan);

    ResponseEntity<JSONObject> cache(Integer douBanId);
}
