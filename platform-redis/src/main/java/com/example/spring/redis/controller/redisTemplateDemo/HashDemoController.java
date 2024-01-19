package com.example.spring.redis.controller.redisTemplateDemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @ClassName HashDemoController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 17:17
 * @Version 1.0
 **/
@RestController
@RequestMapping("/redisHash")
public class HashDemoController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @GetMapping("/demo1")
    public void demo1() {
        //put方法
        redisTemplate.opsForHash().put("k1", "name", "frank");
        redisTemplate.opsForHash().put("k1", "age", "22");
        redisTemplate.opsForHash().put("k1", "height", "176");

        //hashKey不存在时，才设值
        //redisTemplate.opsForHash().putIfAbsent(key, hashKey, value)
    }

    @GetMapping("/demo2")
    public void demo2() {
        //putAll方法
        Map<String, String> data = new HashMap<>();
        data.put("name", "jack ma");
        data.put("company", "alibaba");
        data.put("age", "500");
        redisTemplate.opsForHash().putAll("k2", data);
    }

    @GetMapping("/demo3")
    public void demo3() {
        //delete方法，删除key对应的hash的hashkey及其value
        redisTemplate.opsForHash().delete("k2", "name");
    }

    @GetMapping("/demo4")
    public void demo4() {
        //hasKey方法，确定hashkey是否存在
        System.out.println(redisTemplate.opsForHash().hasKey("k2", "name"));
    }

    @GetMapping("/demo5")
    public void demo5() {
        //get方法，根据key和hashkey找出对应的值
        System.out.println(redisTemplate.opsForHash().get("k1", "name"));
    }

    @GetMapping("/demo6")
    public void demo6() {
        //multiGet方法，根据key和多个hashkey找出对应的多个值
        Collection<Object> keys = new ArrayList<>();
        keys.add("name");
        keys.add("age");
        System.out.println(redisTemplate.opsForHash().multiGet("k1", keys));
    }

    @GetMapping("/demo7")
    public void demo7() {
        //increment方法，对key和hashkey对应的值进行增加操作
        //增加长整形（无法对浮点数据使用本方法）
        System.out.println(redisTemplate.opsForHash().increment("k1", "age", 1));
        //增加浮点型（可以对整形数据使用本方法）
        System.out.println(redisTemplate.opsForHash().increment("k1", "age", 1.0));
    }

    @GetMapping("/demo8")
    public void demo8() {
        //keys方法，获取key对应的hash表的所有key
        Set<Object> keys = redisTemplate.opsForHash().keys("k1");
        System.out.println(keys);

        //values方法，获取key对应的hash表的所有value
        List<Object> values = redisTemplate.opsForHash().values("k1");
        System.out.println(values);
    }

    @GetMapping("/demo9")
    public void demo9() {
        //keys方法，获取key对应的hash表的大小
        long size = redisTemplate.opsForHash().size("k1");
        System.out.println(size);
    }

    @GetMapping("/demo10")
    public void demo10() {
        //keys方法，获取key对应的hash表的所有键值对
        Map<Object, Object> entries = redisTemplate.opsForHash().entries("k1");
        System.out.println(entries);
    }


}
