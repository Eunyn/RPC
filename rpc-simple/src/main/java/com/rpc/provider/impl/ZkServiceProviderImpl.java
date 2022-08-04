package com.rpc.provider.impl;

import com.rpc.config.RpcServiceConfig;
import com.rpc.extension.ExtensionLoader;
import com.rpc.provider.ServiceProvider;
import com.rpc.registry.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: ZkServiceProviderImpl.java
 * @Description: zookeeper 服务提供实现
 * @CreateTime: 2022/7/31 16:10:00
 **/
public class ZkServiceProviderImpl implements ServiceProvider {

    private final Logger logger = LoggerFactory.getLogger(ZkServiceProviderImpl.class);

    private final Map<String, Object> serviceMap;
    private final Set<String> registeredService;
    private final ServiceRegistry serviceRegistry;


    public ZkServiceProviderImpl(){
        serviceMap = new ConcurrentHashMap<>();
        registeredService = ConcurrentHashMap.newKeySet();
        serviceRegistry = ExtensionLoader.getExtensionLoader(ServiceRegistry.class).getExtension("redis");
    }


    @Override
    public void addService(RpcServiceConfig rpcServiceConfig) {
        String rpcServiceName = rpcServiceConfig.getServiceName();
        if (registeredService.contains(rpcServiceName))
            return;
        registeredService.add(rpcServiceName);
        serviceMap.put(rpcServiceName, rpcServiceConfig.getService());
        logger.info("Add service: {} and interface: {}", rpcServiceName, rpcServiceConfig.getService().getClass().getInterfaces());
    }

    @Override
    public Object getService(String rpcServiceName) {
        Object service = serviceMap.get(rpcServiceName);
        if (service == null)
            throw new RuntimeException("SERVICE CAN NOT BE FOUND");
        return service;
    }

    @Override
    public void publishService(RpcServiceConfig rpcServiceConfig) {
        try {
            String host = InetAddress.getLocalHost().getHostAddress();
            this.addService(rpcServiceConfig);
            serviceRegistry.registerService(rpcServiceConfig.getServiceName(), new InetSocketAddress(host, 8080));
        } catch (UnknownHostException e) {
            logger.error("occur exception when getHostAddress", e);
        }
    }
}
