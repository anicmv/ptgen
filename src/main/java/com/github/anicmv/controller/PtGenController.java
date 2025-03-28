package com.github.anicmv.controller;

import cn.hutool.json.JSONObject;
import com.github.anicmv.entity.DouBan;
import com.github.anicmv.service.PtGenService;
import com.github.anicmv.util.PtGenUtil;
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
    public ResponseEntity<String> ptGenHandle(@RequestParam String douban) {
        try {
            Integer douBanId = PtGenUtil.extractDouBanId(douban);
            return service.ptGen(douBanId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/detail")
    public ResponseEntity<JSONObject> detailHandle(@RequestParam String douban) {
        try {
            Integer douBanId = PtGenUtil.extractDouBanId(douban);
            return service.detail(douBanId);
        } catch (IllegalArgumentException e) {
            return PtGenUtil.error(e.getMessage());
        }
    }


    @GetMapping("/old")
    public ResponseEntity<String> oldHandle(@RequestParam String douban) {
        try {
            Integer douBanId = PtGenUtil.extractDouBanId(douban);
            return service.oldData(douBanId);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @PostMapping("/save")
    public ResponseEntity<JSONObject> saveHandle(@RequestBody DouBan douBan) {
        return service.save(douBan);
    }


    @GetMapping("/cache")
    public ResponseEntity<JSONObject> cacheHandle(@RequestParam String douban) {
        try {
            Integer douBanId = PtGenUtil.extractDouBanId(douban);
            return service.cache(douBanId);
        } catch (IllegalArgumentException e) {
            return PtGenUtil.error(e.getMessage());
        }
    }
}
