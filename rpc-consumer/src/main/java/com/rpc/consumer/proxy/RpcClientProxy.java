package com.rpc.consumer.proxy;

import com.rpc.consumer.client.NettyRpcClient;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.remoting.dto.RpcResponse;
import com.rpc.serialize.Serializer;
import com.rpc.serialize.hessian.HessianSerializer;
import com.rpc.utils.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcClientProxy.java
 * @Description: 客户端代理类
 * @CreateTime: 2022/7/28 16:44:00
 **/
@Component
public class RpcClientProxy {

    private final static Map<Class, Object> SERVICE_PROXY = new HashMap<>();

    @Autowired
    private NettyRpcClient rpcClient;

    public Object getProxy(Class serviceClass) {
        if (rpcClient == null)
            throw new RuntimeException("rpcClient 注入失败");

        // 从缓存中查找
        Object proxy = SERVICE_PROXY.get(serviceClass);
        if (proxy == null) {
            // 创建代理对象
            proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{serviceClass},
                    (proxy1, method, args) -> {
                        // 1. 封装请求对象
                        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),
                                method.getDeclaringClass().getName(),
                                method.getName(),
                                method.getParameterTypes(),
                                args);

                        String interfacesName = method.getDeclaringClass().getName();
//                        String serviceDiscovery = RedisUtil.serviceDiscovery(interfacesName);
//                        if (serviceDiscovery == null) {
//                            throw new RuntimeException("服务端没有该服务");
//                        } else {
//                            System.out.println("接口名称：" + interfacesName);
//                            System.out.println("实现类：" + serviceDiscovery);
//                        }

                        try {
                            // Hessian 序列化为字节数组
                            Serializer serializer = new HessianSerializer();
                            byte[] bytes = serializer.serializer(rpcRequest);

                            // 2. 发送消息
                            byte[] msg = rpcClient.send(bytes);

                            // 3. 将消息转化
                            RpcResponse rpcResponse = serializer.deserializer(msg, RpcResponse.class);

                            if (rpcResponse.getError() != null) {
                                throw new RuntimeException(rpcResponse.getError());
                            }
                            if (rpcResponse.getResult() != null) {
                                // return JSON.parseObject(rpcResponse.getResult().toString(), method.getReturnType());
                                return rpcResponse.getResult();
                            }

                            return null;
                        } catch (Exception e) {
                            e.printStackTrace();
                            throw e;
                        }
                    });

            // 放入缓存
            SERVICE_PROXY.put(serviceClass, proxy);
        }

        return proxy;
    }
}
