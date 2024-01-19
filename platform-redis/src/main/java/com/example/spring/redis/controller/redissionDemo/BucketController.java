package com.example.spring.redis.controller.redissionDemo;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName BucketController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/17 16:10
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/redisson/bucket")
@Api(tags = "redisson 操作", consumes = "application/json", produces = "application/json", protocols = "http")
public class BucketController {

    @Autowired
    private RedissonClient redissonClient;

    /**
     * 将对象保存到 Redis 的 RBucket 中
     *
     * @param key   存储的键
     * @param value 要存储的对象
     */
    public void setObjectToBucket(String key, Object value) {
        // 通过 key 获取一个 RBucket 对象
        RBucket<Object> bucket = redissonClient.getBucket(key);
        // 使用 set 方法将对象存储到 RBucket 中
        bucket.set(value);
    }

    /**
     * 从 Redis 的 RBucket 中获取对象
     *
     * @param key 存储的键
     * @return 存储的对象
     */
    public Object getObjectFromBucket(String key) {
        // 通过 key 获取一个 RBucket 对象
        RBucket<Object> bucket = redissonClient.getBucket(key);
        // 使用 get 方法从 RBucket 中获取对象
        return bucket.get();
    }

    /**
     * 从 Redis 的 RBucket 中删除对象
     *
     * @param key 存储的键
     */
    public void removeObjectFromBucket(String key) {
        // 通过 key 获取一个 RBucket 对象
        RBucket<Object> bucket = redissonClient.getBucket(key);
        // 使用 delete 方法从 RBucket 中删除对象
        bucket.delete();
    }
}
