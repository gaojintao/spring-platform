package com.example.spring.redis.controller.redissionDemo;

import com.example.spring.core.entity.ResultData;
import com.example.spring.core.enums.ReturnCode;
import com.example.spring.core.util.ConvertUtil;
import com.example.spring.redis.entity.vo.Student;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RBinaryStream;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.OutputStream;

/**
 * @ClassName BinaryStreamController
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/17 17:37
 * @Version 1.0
 **/
@Slf4j
@RestController
@RequestMapping("/BinaryStream")
@Api(tags = "分布式对象二进制流的操作", consumes = "application/json", produces = "application/json", protocols = "http")
public class BinaryStreamController {
    @Autowired
    private RedissonClient redissonClient;

    /**
     * 将二进制数据写入 Redis
     *
     * @param key           用于存储的键
     * @param student 要存储的二进制数据输入流
     */
    @PostMapping ("/writeToRedis")
    @ApiOperation(value = "将二进制数据写入 Redis", notes = "将二进制数据写入 Redis")
    public ResultData<String> writeToRedis(@RequestParam("key") String key, @RequestBody Student student) throws Exception {
        // 通过 key 获取一个 RBinaryStream 对象
        RBinaryStream stream = redissonClient.getBinaryStream(key);
        ConvertUtil convertUtil = new ConvertUtil();
        InputStream binaryContent = convertUtil.convertToInputStream(student);
        // 获取 RBinaryStream 的 OutputStream
        try (OutputStream os = stream.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            // 从输入流读取数据并写入到 Redis
            while ((bytesRead = binaryContent.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        }
        return ResultData.success("添加成功");
    }

    /**
     * 从 Redis 中读取二进制数据
     *
     * @param key 用于检索的键
     * @return 存储的二进制数据输出流
     */
    @GetMapping ("/readFromRedis")
    @ApiOperation(value = "从 Redis 中读取二进制数据", notes = "从 Redis 中读取二进制数据")

    public ResultData<Student> readFromRedis(@RequestParam("key")String key) {
        // 通过 key 获取一个 RBinaryStream 对象
        RBinaryStream stream = redissonClient.getBinaryStream(key);
        Student student = new Student();
       try {
           ObjectInputStream objectInputStream = new ObjectInputStream(stream.getInputStream());

           // 从 ObjectInputStream 中读取对象并转换为实体类
           Object object = objectInputStream.readObject();
           student = (Student) object;
        }catch (Exception e){
          log.error("失败");
       }
        // 返回 RBinaryStream 的 InputStream
        return ResultData.success(student);

    }

    /**
     * 从 Redis 中删除与特定键关联的二进制数据
     *
     * @param key 要删除的数据的键
     */
    @ApiOperation(value = "从 Redis 中删除与特定键关联的二进制数据", notes = "从 Redis 中删除与特定键关联的二进制数据")
    @GetMapping ("/deleteFromRedis")
    public ResultData<String> deleteFromRedis(@RequestParam("key")String key) {
        // 通过 key 获取一个 RBucket 对象
        RBucket<Object> bucket = redissonClient.getBucket(key);
        // 使用 delete 方法删除与 key 关联的数据
        bucket.delete();

        return ResultData.success(ReturnCode.RC100.getMessage());
    }
}
