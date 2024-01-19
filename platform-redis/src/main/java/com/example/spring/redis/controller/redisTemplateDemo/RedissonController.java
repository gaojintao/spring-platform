package com.example.spring.redis.controller.redisTemplateDemo;

import com.example.spring.core.entity.ResultData;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName RedissonController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 16:17
 * @Version 1.0
 **/
@RestController
public class RedissonController {
    @Autowired
    private RedissonClient redissonClient;

    @GetMapping(value = "/redisson/{key}")
    public ResultData<String> redissonTest(@PathVariable("key") String lockKey){
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            rLock.lock();
            Thread.sleep(5000);
        } catch (Exception e){

        } finally {
            rLock.unlock();
        }
        return ResultData.success("已成功解锁");
    }


}
