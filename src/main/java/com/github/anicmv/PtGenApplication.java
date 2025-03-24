package com.github.anicmv;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author anicmv
 * @date 2025/3/22 19:54
 * @description 启动类
 */
@SpringBootApplication
@MapperScan("com.github.anicmv.mapper")
public class PtGenApplication {

    public static void main(String[] args) {
        SpringApplication.run(PtGenApplication.class, args);
    }

}
