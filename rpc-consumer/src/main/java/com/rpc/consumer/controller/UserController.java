package com.rpc.consumer.controller;

import com.rpc.annotation.RpcReference;
import com.rpc.api.HelloService;
import com.rpc.api.IUserService;
import com.rpc.pojo.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: UserController.java
 * @Description: 用户控制类
 * @CreateTime: 2022/7/24 17:53:00
 **/
@Component
public class UserController {

    @RpcReference
    IUserService userService;

    @RpcReference
    HelloService helloService;

//    @RequestMapping("/getUserById")
    public User getUserById(Integer id) {
        if (userService == null)
            return new User(2333, "fuck, what happened?");

        return userService.getById(id);
    }

    public String sayHello(String s) {
        return helloService.sayHello(s);
    }
}
