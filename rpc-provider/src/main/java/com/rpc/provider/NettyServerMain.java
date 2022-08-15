package com.rpc.provider;

import com.rpc.annotation.RpcScan;
import com.rpc.api.HelloService;
import com.rpc.api.IUserService;
import com.rpc.config.RpcServiceConfig;
import com.rpc.provider.service.HelloServiceImpl;
import com.rpc.provider.service.UserServiceImpl;
import com.rpc.remoting.transport.netty.server.NettyRpcServer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyServerMain.java
 * @Description: Netty 服务端
 * @CreateTime: 2022/8/8 16:55:00
 **/
@RpcScan(basePackage = {"com.rpc"})
public class NettyServerMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyServerMain.class);
        NettyRpcServer nettyRpcServer = (NettyRpcServer) applicationContext.getBean("nettyRpcServer");

//        IUserService service = new UserServiceImpl();
        HelloService service = new HelloServiceImpl();
        RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();

        rpcServiceConfig.setService(service);
        nettyRpcServer.registerService(rpcServiceConfig);
        nettyRpcServer.start();
    }
}
