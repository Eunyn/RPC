package com.rpc.consumer.proxy;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.rpc.common.RpcRequest;
import com.rpc.common.RpcResponse;
import com.rpc.consumer.client.NettyRpcClient;
import com.rpc.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcClientProxy.java
 * @Description: 客户端代理类
 * @CreateTime: 2022/7/25 17:10:00
 **/
@Component
public class RpcClientProxy {
    private final static Map<Class, Object> SERVICE_PROXY = new HashMap<>();

    @Autowired
    NettyRpcClient rpcClient;

    public Object getProxy(Class serviceClass) {
        // 从缓存中查找
        Object proxy = SERVICE_PROXY.get(serviceClass);
        if (proxy == null) {
            // 创建代理对象
            proxy = Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[]{serviceClass},
                    new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            // 1. 封装请求对象
                            RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),
                                    method.getDeclaringClass().getName(),
                                    method.getName(),
                                    method.getParameterTypes(),
                                    args);

                            try {
                                // 2. 发送消息
                                Object msg = rpcClient.send(JSON.toJSONString(rpcRequest));
                                // 3. 将消息转化
                                RpcResponse rpcResponse = JSON.parseObject(msg.toString(), RpcResponse.class);
                                if (rpcResponse.getError() != null) {
                                    throw new RuntimeException(rpcResponse.getError());
                                }
                                if (rpcResponse.getResult() != null) {
                                    return JSON.parseObject(rpcResponse.getResult().toString(), method.getReturnType());
                                }

                                return null;
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw e;
                            }
                        }
                    });

            // 放入缓存
            SERVICE_PROXY.put(serviceClass, proxy);

            return proxy;
        } else {
            return proxy;
        }
    }
}
