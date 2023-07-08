package com.example.springtest_backend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.springtest_backend.mapper")
public class SpringtestBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringtestBackendApplication.class, args);
    }

}
