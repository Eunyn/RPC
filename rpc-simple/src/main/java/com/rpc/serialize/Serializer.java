package com.rpc.serialize;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: Serializer.java
 * @Description: 序列化接口
 * @CreateTime: 2022/7/27 14:14:00
 **/
public interface Serializer {

    /**
     * 序列化
     *
     * @param obj 要序列化的对象
     * @return 字节数组
     */
    byte[] serializer(Object obj);


    /**
     * 反序列化
     *
     * @param bytes 序列化后的数组
     * @param clazz 目标类
     * @param <T> 类的类型
     * @return 反序列化的对象
     */
    <T> T deserializer(byte[] bytes, Class<T> clazz);
}
