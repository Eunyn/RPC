package com.rpc.consumer;

import com.rpc.api.HelloService;
import com.rpc.api.IUserService;
import com.rpc.config.RpcServiceConfig;
import com.rpc.pojo.User;
import com.rpc.proxy.RpcClientProxy;
import com.rpc.remoting.transport.RpcRequestTransport;
import com.rpc.remoting.transport.socket.SocketRpcClient;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: SocketClientMain.java
 * @Description: socket 客户端
 * @CreateTime: 2022/8/3 15:04:00
 **/
public class SocketClientMain {
    public static void main(String[] args) {
        RpcRequestTransport rpcRequestTransport = new SocketRpcClient();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcRequestTransport, rpcServiceConfig);
//        IUserService proxy = rpcClientProxy.getProxy(IUserService.class);
//        User user = proxy.getById(1);
//        System.out.println(user.toString());

        HelloService proxy = rpcClientProxy.getProxy(HelloService.class);
        String rpc = proxy.sayHello("RPC");
        System.out.println(rpc);
    }
}
