package com.github.anicmv.enums;

import lombok.Getter;

/**
 * @author anicmv
 * @date 2025/3/15 19:24
 * @description 图片返回参数枚举
 */
@Getter
public enum PtGenEnum {

    SUCCESS("success", 200),
    FAILURE("failure", -1);


    private final String result;
    private final Integer code;


    PtGenEnum(String result, Integer code) {
        this.result = result;
        this.code = code;
    }
}
