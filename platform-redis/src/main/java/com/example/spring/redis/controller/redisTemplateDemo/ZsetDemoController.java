package com.example.spring.redis.controller.redisTemplateDemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * @ClassName ZsetDemoController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 17:24
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/redisZSet")
public class ZsetDemoController {
    @Autowired
    private StringRedisTemplate redisTemplate;
    /**
     * 增加有序集合
     *
     * @param key
     * @param value
     * @param seqNo
     * @return
     */
    @GetMapping("/demo1/{key}/{value}/{seqNo}")
    public Boolean demo1(@PathVariable("key")String key, @PathVariable("value")String value,@PathVariable("seqNo")double seqNo) {

        try {
            return redisTemplate.opsForZSet().add(key, value, seqNo);
        } catch (Exception e) {
            log.error("[RedisUtils.addZset] [error]", e);
            return false;
        }
    }


    /**
     * 获取zset集合数量
     *
     * @param key
     * @return
     */
    @GetMapping("/demo2/{key}")
    public Long demo2(@PathVariable("key")String key) {
        try {
            return redisTemplate.opsForZSet().size(key);
        } catch (Exception e) {
            log.error("[RedisUtils.countZset] [error] [key is {}]", key, e);
            return 0L;
        }
    }


    /**
     * 获取zset指定范围内的集合
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    @GetMapping("/demo3/{key}/{start}/{end}")
    public Set<Object> demo3(@PathVariable("key")String key, @PathVariable("start")long start,@PathVariable("end")long end) {

        try {
            return Collections.singleton(redisTemplate.opsForZSet().range(key, start, end));
        } catch (Exception e) {
            log.error("[RedisUtils.rangeZset] [error] [key is {},start is {},end is {}]", key, start, end, e);
            return null;
        }
    }

    /**
     * 根据key和value移除指定元素
     *
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/demo4/{key}/{value}")
    public Long demo4(@PathVariable("key") String key, @PathVariable("value") String value) {
        return redisTemplate.opsForZSet().remove(key, value);
    }


    /**
     * 获取对应key和value的score
     *
     * @param key
     * @param value
     * @return
     */
    @GetMapping("/demo5/{key}/{value}")
    public Double demo5(@PathVariable("key") String key, @PathVariable("value") String value) {
        return redisTemplate.opsForZSet().score(key, value);
    }


    /**
     * 指定范围内元素排序
     *
     * @param key
     * @param v1
     * @param v2
     * @return
     */
    @GetMapping("/demo6/{key}/{v1}/{v2}")
    public Set<Object> demo6( @PathVariable("key")String key,  @PathVariable("v1")Double v1,  @PathVariable("v2")Double v2) {
        return Collections.singleton(redisTemplate.opsForZSet().rangeByScore(key, v1, v2));
    }



    /**
     * 指定元素增加指定值
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    @GetMapping("/demo7/{key}/{value}/{score}")
    public Object demo7(@PathVariable("key")String key, @PathVariable("value") Object value, @PathVariable("score") double score) {
        return redisTemplate.opsForZSet().incrementScore(key, value.toString(), score);
    }


    /**
     * 排名
     *
     * @param key
     * @param obj
     * @return
     */
    @GetMapping("/demo8/{key}/{obj}")
    public Object demo8(@PathVariable("key") String key, @PathVariable("obj") Object obj) {
        return redisTemplate.opsForZSet().rank(key, obj);
    }

}
