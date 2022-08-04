package com.rpc.registry.redis;

import com.rpc.registry.ServiceRegistry;
import com.rpc.utils.RedisUtil;

import java.net.InetSocketAddress;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RedisServiceRegistryImpl.java
 * @Description: redis 服务注册实现类
 * @CreateTime: 2022/7/31 14:38:00
 **/
public class RedisServiceRegistryImpl implements ServiceRegistry {
    @Override
    public void registerService(String rpcServiceName, InetSocketAddress inetSocketAddress) {
        RedisUtil.serviceRegistry(rpcServiceName, inetSocketAddress.toString());
    }
}
