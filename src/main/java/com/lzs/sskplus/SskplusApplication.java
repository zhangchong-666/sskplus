package com.lzs.sskplus;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
@MapperScan("com.lzs.sskplus.mapper")
@SpringBootApplication
public class SskplusApplication {

    public static void main(String[] args) {
        SpringApplication.run(SskplusApplication.class, args);
    }

}
