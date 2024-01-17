package com.example.spring.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @ClassName PlatformCoreApplication
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 15:15
 * @Version 1.0
 **/

@SpringBootApplication
//@EnableFeignClients
@EnableCaching
public class PlatformCoreApplication {
    public static void main(String[] args) {
        SpringApplication.run(PlatformCoreApplication.class, args);
    }

}

