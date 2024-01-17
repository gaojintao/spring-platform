package com.example.spring.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;

/**
 * @ClassName PlatformRedisApplication
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 15:50
 * @Version 1.0
 **/
@SpringBootApplication
@EnableCaching

public class PlatformRedisApplication {
    public static void main(String[] args) {
        System.out.println("qidong");
        SpringApplication.run(PlatformRedisApplication.class, args);
    }

}
