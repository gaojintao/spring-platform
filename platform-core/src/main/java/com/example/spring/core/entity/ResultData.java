package com.example.spring.core.entity;

import com.example.spring.core.enums.ReturnCode;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @ClassName ResultData
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/15 18:26
 * @Version 1.0
 **/
@Data
public class ResultData<T> {
    private int status;
    private String message;
    private T data;
    private LocalDateTime timestamp;

    public  ResultData(){
        this.timestamp = LocalDateTime.now();
    }

    public static <T> ResultData<T> success(T data){
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(ReturnCode.RC100.getCode());
        resultData.setMessage(ReturnCode.RC100.getMessage());
        resultData.setData(data);
        return resultData;
    }

    public static <T> ResultData<T> fail(int code,String message){
        ResultData<T> resultData = new ResultData<>();
        resultData.setStatus(code);
        resultData.setMessage(message);
        return resultData;
    }
    
}
