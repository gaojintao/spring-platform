package com.example.spring.redis.config;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * @ClassName RedisConfig
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 16:39
 * @Version 1.0
 **/
@SpringBootConfiguration
public class RedisConfig {
    @Bean
    public RedisTemplate<Object,Object> redisTemplate(RedissonConnectionFactory redissonConnectionFactory){
//        RedisTemplate<String,Object> redisTemplate = new RedisTemplate<>();
//        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
//
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//        redisTemplate.setValueSerializer(genericToStringSerializer);
////        redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//        redisTemplate.setHashValueSerializer(new GenericFastJsonRedisSerializer());
//
//        redisTemplate.setConnectionFactory(redissonConnectionFactory);
//        return redisTemplate;
        // 1.创建 redisTemplate 模版
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        // 2.关联 redisConnectionFactory
        template.setConnectionFactory(redissonConnectionFactory);
        // 3.创建 序列化类
        GenericToStringSerializer genericToStringSerializer = new GenericToStringSerializer(Object.class);
        // 6.序列化类，对象映射设置
        // 7.设置 value 的转化格式和 key 的转化格式
        template.setValueSerializer(genericToStringSerializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.afterPropertiesSet();
        return template;
    }
}
