package com.example.spring.redis.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

/**
 * @ClassName RedissonConfig
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 16:43
 * @Version 1.0
 **/
@SpringBootConfiguration
public class RedissonConfig {
    @Value("${spring.redis.url}")
    private String redisHost;
    @Value("${spring.redis.port}")
    private String redisPort;

    @Bean
    public RedissonClient redissonClient(){
        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+redisHost+":"+redisPort);
        return Redisson.create(config);
    }
}
