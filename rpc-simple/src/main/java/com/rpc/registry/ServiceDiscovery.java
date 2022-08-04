package com.rpc.registry;

import com.rpc.annotation.SPI;
import com.rpc.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: ServiceDiscovery.java
 * @Description: 服务发现
 * @CreateTime: 2022/7/28 15:00:00
 **/
@SPI
public interface ServiceDiscovery {

    /**
     * lookup service by rpcServiceName
     *
     * @param rpcRequest rpcRequest rpc service pojo
     * @return service address
     */
    InetSocketAddress lookupService(RpcRequest rpcRequest);
}
