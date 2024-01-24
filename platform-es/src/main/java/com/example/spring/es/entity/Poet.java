package com.example.spring.es.entity;

import lombok.Data;

/**
 * @ClassName Poet
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/24 10:02
 * @Version 1.0
 **/
@Data
public class Poet {
    private Integer age;
    private String name;
    private String poems;

    private String about;
    private String success;

    public Poet(){

    }
    public Poet(Integer age, String name, String poems, String about, String success) {
        this.age = age;
        this.name = name;
        this.poems = poems;
        this.about = about;
        this.success = success;
    }
}
