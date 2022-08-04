package com.rpc.loadbalance.impl;

import com.rpc.loadbalance.AbstractLoadBalance;
import com.rpc.remoting.dto.RpcRequest;

import java.util.List;
import java.util.Random;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RandomLoadBalance.java
 * @Description: Implementation of random load balancing strategy
 * @CreateTime: 2022/8/2 15:59:00
 **/
public class RandomLoadBalance extends AbstractLoadBalance {
    @Override
    protected String doSelect(List<String> serviceAddress, RpcRequest rpcRequest) {
        Random random = new Random();
        return serviceAddress.get(random.nextInt(serviceAddress.size()));
    }
}
