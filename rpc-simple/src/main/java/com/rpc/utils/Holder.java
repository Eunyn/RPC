package com.rpc.utils;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: Holder.java
 * @Description: TODO
 * @CreateTime: 2022/7/31 16:47:00
 **/
public class Holder<T> {
    private volatile T value;

    public T get() {
        return value;
    }

    public void set(T value) {
        this.value = value;
    }
}
