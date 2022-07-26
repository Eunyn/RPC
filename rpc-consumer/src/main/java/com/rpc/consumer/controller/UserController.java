package com.rpc.consumer.controller;

import com.rpc.api.IUserService;
import com.rpc.consumer.anno.RpcReference;
import com.rpc.pojo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: UserController.java
 * @Description: 用户控制类
 * @CreateTime: 2022/7/24 17:53:00
 **/
@RestController
@RequestMapping("/user")
public class UserController {

    @RpcReference
    IUserService userService;

    @RequestMapping("/getUserById")
    public User getUserById(int id) {

        return userService.getById(id);
    }
}
