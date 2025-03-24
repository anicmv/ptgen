package com.github.anicmv.dto;

import com.github.anicmv.entity.DouBan;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;

/**
 * @author anicmv
 * @date 2025/3/23 17:39
 * @description 豆瓣数据
 */
@Log4j2
@Getter
@Setter
@Builder
public class ResponseData {
    private DouBan douBan;
    private DouBanPage douBanPage;
    private DouBanDetail douBanDetail;
    private String ptGen;

    public ResponseData buildPtGen() {
        ptGen = douBan.buildPtGen();
        return this;
    }
}
