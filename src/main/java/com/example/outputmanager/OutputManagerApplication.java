package com.example.outputmanager;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.outputmanager.mapper")
public class OutputManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(OutputManagerApplication.class, args);
    }
}