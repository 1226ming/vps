package com.example.vps;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@MapperScan("com.example.vps.**.mapper")
@SpringBootApplication
public class VpsApplication {
    public static void main(String[] args) {
        SpringApplication.run(VpsApplication.class, args);
    }
}
