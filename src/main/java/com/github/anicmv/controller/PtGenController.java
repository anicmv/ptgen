package com.github.anicmv.controller;

import cn.hutool.json.JSONObject;
import com.github.anicmv.entity.DouBan;
import com.github.anicmv.service.PtGenService;
import com.github.anicmv.util.DouBanUtil;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author anicmv
 * @date 2025/3/22 19:54
 * @description 豆瓣接口
 */
@RestController
public class PtGenController {

    @Resource
    private PtGenService service;

    @GetMapping("/ptgen")
    public ResponseEntity<String> ptGen(@RequestParam String url) {
        try {
            Integer douBanId = DouBanUtil.extractDouBanId(url);
            return service.ptGen(douBanId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<JSONObject> detail(@RequestParam String url) {
        try {
            Integer douBanId = DouBanUtil.extractDouBanId(url);
            return service.detail(douBanId);
        } catch (IllegalArgumentException e) {
            return DouBanUtil.error(e.getMessage());
        }
    }


    @GetMapping("/old")
    public ResponseEntity<String> oldData(@RequestParam String url) {
        try {
            Integer douBanId = DouBanUtil.extractDouBanId(url);
            return service.oldData(douBanId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/save")
    public ResponseEntity<JSONObject> save(@RequestBody DouBan douBan) {
        return service.save(douBan);
    }


    @GetMapping("/cache")
    public ResponseEntity<JSONObject> cache(@RequestParam String url) {
        try {
            Integer douBanId = DouBanUtil.extractDouBanId(url);
            return service.cache(douBanId);
        } catch (IllegalArgumentException e) {
            return DouBanUtil.error(e.getMessage());
        }
    }
}
