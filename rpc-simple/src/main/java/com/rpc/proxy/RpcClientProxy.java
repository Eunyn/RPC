package com.rpc.proxy;

import com.rpc.config.RpcServiceConfig;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.remoting.dto.RpcResponse;
import com.rpc.remoting.transport.RpcRequestTransport;
import com.rpc.remoting.transport.socket.SocketRpcClient;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcClientProxy.java
 * @Description: 代理
 * @CreateTime: 2022/8/3 15:17:00
 **/
public class RpcClientProxy implements InvocationHandler {

    private final static String INTERFACE_NAME = "interfaceName";

    private final RpcRequestTransport rpcRequestTransport;
    private final RpcServiceConfig rpcServiceConfig;

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport, RpcServiceConfig rpcServiceConfig) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = rpcServiceConfig;
    }

    public RpcClientProxy(RpcRequestTransport rpcRequestTransport) {
        this.rpcRequestTransport = rpcRequestTransport;
        this.rpcServiceConfig = new RpcServiceConfig();
    }

    /**
     * get the proxy object
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T getProxy(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest rpcRequest = new RpcRequest(UUID.randomUUID().toString(),
                method.getDeclaringClass().getName(),
                method.getName(),
                method.getParameterTypes(),
                args);

        RpcResponse rpcResponse = null;
        if (rpcRequestTransport instanceof SocketRpcClient) {
            rpcResponse = (RpcResponse) rpcRequestTransport.sendRpcRequest(rpcRequest);
        }

        assert rpcResponse != null;
        return rpcResponse.getResult();
    }
}
