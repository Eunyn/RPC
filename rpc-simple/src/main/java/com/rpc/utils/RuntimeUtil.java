package com.rpc.utils;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RuntimeUtil.java
 * @Description: 获取 CPU 核心数
 * @CreateTime: 2022/8/8 15:03:00
 **/
public class RuntimeUtil {

    /**
     * 获取 CPU 核心数
     *
     * @return cpu 核心数
     */
    public static int cpus() {
        return Runtime.getRuntime().availableProcessors();
    }
}
