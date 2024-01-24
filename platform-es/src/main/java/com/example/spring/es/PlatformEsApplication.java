package com.example.spring.es;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @ClassName PlatformEsApplication
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/19 16:58
 * @Version 1.0
 **/
@SpringBootApplication(scanBasePackages = "com.example.spring.es")
@EnableSwagger2
public class PlatformEsApplication {
    public static void main(String[] args) {
        System.out.println("qidong");
        SpringApplication.run(PlatformEsApplication.class, args);
    }

}
