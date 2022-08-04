package com.rpc.provider;

import com.rpc.api.IUserService;
import com.rpc.config.RpcServiceConfig;
import com.rpc.provider.service.UserServiceImpl;
import com.rpc.remoting.transport.socket.SocketRpcServer;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: SocketServerMain.java
 * @Description: socket 服务端
 * @CreateTime: 2022/8/3 15:29:00
 **/
public class SocketServerMain {
    public static void main(String[] args) {
        IUserService userService = new UserServiceImpl();
        SocketRpcServer socketRpcServer = new SocketRpcServer();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
        rpcServiceConfig.setService(userService);
        socketRpcServer.registerService(rpcServiceConfig);
        socketRpcServer.start();
    }
}
