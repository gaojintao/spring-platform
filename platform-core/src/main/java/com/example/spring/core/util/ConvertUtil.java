package com.example.spring.core.util;

import io.swagger.models.auth.In;

import java.io.*;

/**
 * @ClassName ConvertUtil
 * @Description TODO
 * @Author GaoJinTao
 * @Date 2024/01/18 12:22
 * @Version 1.0
 **/

public class ConvertUtil<T> {
    public InputStream convertToInputStream(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        // 将对象写入ByteArrayOutputStream
        objectOutputStream.writeObject(object);
        objectOutputStream.flush();
        objectOutputStream.close();

        // 从ByteArrayOutputStream中获取ByteArrayInputStream
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return new ByteArrayInputStream(byteArray);
    }


}
