package com.example.spring.es.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * @ClassName Product
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/22 11:03
 * @Version 1.0
 **/
@Data
public class Product implements Serializable {
    private String id;
    private String name;
    private int price;



    public Product() {
    }

    public Product(String id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }
    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}
