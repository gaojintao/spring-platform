package com.example.spring.redis.controller.redissionDemo;

import com.example.spring.core.entity.ResultData;
import com.example.spring.core.enums.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.GeoEntry;
import org.redisson.api.GeoPosition;
import org.redisson.api.RGeo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @ClassName RGeoController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/19 11:18
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/redisson/RGeo")
@Api(tags = "redisson 操作", consumes = "application/json", produces = "application/json", protocols = "http")
public class RGeoController {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 添加地理位置信息
     *
     * @param key      用于存储的键
     * @param latitude 纬度
     * @param longitude 经度
     * @param member   位置名称或标识符
     */
//    @PostMapping("/addLocation")
//    @ApiOperation(value = "添加地理位置信息", notes = "添加地理位置信息")
//    public ResultData<String> addLocation(String key, double latitude, double longitude, String member) {
//        // 获取 RGeo 对象
//        RGeo<String> geo = redissonClient.getGeo(key);
//        // 添加地理位置信息
//        geo.add(new GeoEntry(longitude, latitude, member));
//        return ResultData.success(ReturnCode.RC100.getMessage());
//    }

    /**
     * 根据位置名称或标识符获取地理位置
     *
     * @param key    用于检索的键
     * @param member 位置名称或标识符
     * @return 地理位置坐标
     */
//    public Collection<Map.Entry<String, org.redisson.client.protocol.ScoredEntry<org.redisson.api.geo.GeoPosition>>> getLocation(String key, String member) {
//        // 获取地理空间键（GeoKey）的实例
//        RGeo<String> geo = redissonClient.getGeo(key);
//        return null;
//    }

    /**
     * 从 Redis 中删除地理位置信息
     *
     * @param key    用于删除的键
     * @param members 要删除的位置名称或标识符
     */
    public void removeLocation(String key, String... members) {
        // 获取 RGeo 对象
        RGeo<String> geo = redissonClient.getGeo(key);
        // 删除地理位置信息
        geo.remove(members);
    }
}
