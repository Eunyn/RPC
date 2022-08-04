package com.rpc.registry;

import com.rpc.annotation.SPI;

import java.net.InetSocketAddress;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: ServiceRegistry.java
 * @Description: 服务注册
 * @CreateTime: 2022/7/28 15:01:00
 **/
@SPI
public interface ServiceRegistry {

    /**
     * register service
     *
     * @param rpcServiceName rpc service name
     * @param inetSocketAddress inetSocketAddress service address
     */
    void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress);
}
