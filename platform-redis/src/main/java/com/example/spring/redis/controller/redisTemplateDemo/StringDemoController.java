package com.example.spring.redis.controller.redisTemplateDemo;

import com.example.spring.core.entity.ResultData;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName StringDemoController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 17:16
 * @Version 1.0
 **/
@RestController
@RequestMapping("/redisString")
public class StringDemoController {
    @Autowired
    private RedissonClient redissonClient;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping(value = "/add/{key}")
    public ResultData<String> addStringValue(@PathVariable("key") String key){
        stringRedisTemplate.opsForValue().set(key,key+"添加");
        return ResultData.success(key+"添加成功");
    }
    @GetMapping(value = "/addTime/{key}")
    public ResultData<String> addStringValueTime(@PathVariable("key") String key) throws InterruptedException {
        //设置k,v以及有效时长，TimeUnit为单位
        System.out.println(LocalDateTime.now());
        stringRedisTemplate.opsForValue().set(key, "bpf", 10, TimeUnit.SECONDS);
        Thread.sleep(11000);
        return ResultData.success(stringRedisTemplate.opsForValue().get(key));
    }
    @GetMapping(value = "/addOffset/{key}")
    public ResultData<String> addStringValueOffset(@PathVariable("key") String key) {
        stringRedisTemplate.opsForValue().set(key, "hello world");
        //从偏移量开始对给定key的value进行覆写，若key不存在，则前面偏移量为空
        stringRedisTemplate.opsForValue().set(key, "redis", 6);
        return ResultData.success(stringRedisTemplate.opsForValue().get(key));
    }

    @GetMapping(value = "/addAbsent/{key}")
    public ResultData<String> addStringValueAbsent(@PathVariable("key") String key) {
        stringRedisTemplate.opsForValue().set(key, "test");
        //若key不存在，设值
        stringRedisTemplate.opsForValue().setIfAbsent(key, "test2");
        return ResultData.success(stringRedisTemplate.opsForValue().get(key));
    }
    @GetMapping(value = "/addList")
    public ResultData<List<String>> addStringValueList() {
        //批量k,v设值
        Map<String, String> map = new HashMap<String, String>();
        map.put("k1", "v1");
        map.put("k2", "v2");
        map.put("k3", "v3");
        stringRedisTemplate.opsForValue().multiSet(map);

        //批量取值
        List<String> keys = new ArrayList<String>();
        keys.add("k1");
        keys.add("k2");
        keys.add("k3");
        List<String> values = stringRedisTemplate.opsForValue().multiGet(keys);
        return ResultData.success(values);

        //批量设值，若key不存在，设值
        //redisTemplate.opsForValue().multiSetIfAbsent(map);

    }
    @GetMapping(value = "/addGetOld/{key}")
    public ResultData<String> addGetOld(@PathVariable("key") String key){
        stringRedisTemplate.opsForValue().set(key, "bpf");
        //设值并返回旧值，无旧值返回null
        return ResultData.success(stringRedisTemplate.opsForValue().getAndSet(key, "calvin"));
    }

    @GetMapping(value = "/autoAdd")
    public void demo7() {
        //自增操作，原子性。可以对long进行double自增，但不能对double进行long自增，会抛出异常
        stringRedisTemplate.opsForValue().increment("count", 1);//增量为long
        System.out.println(stringRedisTemplate.opsForValue().get("count"));

        stringRedisTemplate.opsForValue().increment("count", 2);//增量为double
        System.out.println(stringRedisTemplate.opsForValue().get("count"));
    }

    @GetMapping(value = "/keyNotExist")
    public void demo8() {
        //key不存在，设值
        stringRedisTemplate.opsForValue().append("str", "hello");
        System.out.println(stringRedisTemplate.opsForValue().get("str"));
        //key存在，追加
        stringRedisTemplate.opsForValue().append("str", " world");
        System.out.println(stringRedisTemplate.opsForValue().get("str"));

    }

    @GetMapping(value = "/getValueLen")
    public void demo9() {
        stringRedisTemplate.opsForValue().set("key", "hello world");
        //value的长度
        System.out.println(stringRedisTemplate.opsForValue().size("key"));//11
    }

    @GetMapping(value = "/getoffset")
    public void demo10() {
        stringRedisTemplate.opsForValue().set("bitTest","a");
        // 'a' 的ASCII码是 97  转换为二进制是：01100001
        // 'b' 的ASCII码是 98  转换为二进制是：01100010
        // 'c' 的ASCII码是 99  转换为二进制是：01100011

        //因为二进制只有0和1，在setbit中true为1，false为0，因此我要变为'b'的话第六位设置为1，第七位设置为0
        stringRedisTemplate.opsForValue().setBit("bitTest",6, true);
        stringRedisTemplate.opsForValue().setBit("bitTest",7, false);
        System.out.println(stringRedisTemplate.opsForValue().get("bitTest"));

        //判断offset处是true1还是false0
        System.out.println(stringRedisTemplate.opsForValue().getBit("bitTest",7));
    }

    @GetMapping(value = "/query/{key}")
    public ResultData queryStringValue(@PathVariable("key") String key){
        String value = stringRedisTemplate.opsForValue().get(key);
        return ResultData.success("获取key的value："+value);
    }

    @GetMapping(value = "/delete/{key}")
    public ResultData delStringValue(@PathVariable("key") String key){

        stringRedisTemplate.delete(key);
        return ResultData.success("删除key");
    }

}
