package com.rpc.provider;

import com.rpc.annotation.SPI;
import com.rpc.config.RpcServiceConfig;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: ServiceProvider.java
 * @Description: 暴露服务
 * @CreateTime: 2022/7/31 16:03:00
 **/
public interface ServiceProvider {

    /**
     * 通过 rpc 服务配置添加服务
     * @param rpcServiceConfig rpc service related attributes
     */
    void addService(RpcServiceConfig rpcServiceConfig);

    /**
     * 根据服务名称获取服务对象
     * @param rpcServiceName rpc service name
     * @return service object
     */
    Object getService(String rpcServiceName);

    /**
     * 发布服务
     * @param rpcServiceConfig rpc service related attributes
     */
    void publishService(RpcServiceConfig rpcServiceConfig);

}
