package com.rpc.consumer;

import com.rpc.annotation.RpcScan;
import com.rpc.consumer.controller.UserController;
import com.rpc.pojo.User;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyClientMain.java
 * @Description: Netty 客户端
 * @CreateTime: 2022/8/8 17:00:00
 **/
@RpcScan(basePackage = {"com.rpc"})
public class NettyClientMain {
    public static void main(String[] args) {
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext(NettyClientMain.class);
        UserController userController = (UserController)applicationContext.getBean("userController");
        String result = userController.sayHello("Client");
        System.out.println(result);
    }
}
