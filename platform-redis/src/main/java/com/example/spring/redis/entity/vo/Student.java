package com.example.spring.redis.entity.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Student
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/18 12:10
 * @Version 1.0
 **/
@Data
public class Student implements Serializable {
    private String name;
    private Integer age;
}
