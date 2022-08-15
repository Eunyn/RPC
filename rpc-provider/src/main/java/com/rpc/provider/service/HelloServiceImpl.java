package com.rpc.provider.service;

import com.rpc.annotation.RpcService;
import com.rpc.api.HelloService;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: HelloServiceImpl.java
 * @Description: HelloServiceImpl
 * @CreateTime: 2022/8/10 13:50:00
 **/
@RpcService
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String val) {
        return "Hello, " + val;
    }
}
