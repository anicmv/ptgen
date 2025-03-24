package com.github.anicmv.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author anicmv
 * @date 2025/3/23 17:28
 * @description 豆瓣人民
 */
@Data
@Builder
public class Person {
    private String type;
    private String url;
    private String name;
}
