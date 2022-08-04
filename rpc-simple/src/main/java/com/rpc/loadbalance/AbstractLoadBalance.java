package com.rpc.loadbalance;

import com.rpc.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: AbstractLoadBalance.java
 * @Description: Abstract class for a load balancing policy
 * @CreateTime: 2022/8/2 15:55:00
 **/
public abstract class AbstractLoadBalance implements LoadBalance {
    @Override
    public String selectServiceAddress(List<String> serviceAddress, RpcRequest rpcRequest) {
        if (serviceAddress == null || serviceAddress.size() == 0)
            return null;
        if (serviceAddress.size() == 1)
            return serviceAddress.get(0);
        
        return doSelect(serviceAddress, rpcRequest);
    }

    protected abstract String doSelect(List<String> serviceAddress, RpcRequest rpcRequest);
}
