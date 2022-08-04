package com.rpc.loadbalance;

import com.rpc.annotation.SPI;
import com.rpc.remoting.dto.RpcRequest;

import java.util.List;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: LoadBalance.java
 * @Description: Interface to the load balancing policy
 * @CreateTime: 2022/8/2 15:51:00
 **/
@SPI
public interface LoadBalance {

    /**
     * Choose one from the list of existing service addresses list
     * @param serviceUrlList Service address list
     * @param rpcRequest the request of the client
     * @return target service address
     */
    String selectServiceAddress(List<String> serviceUrlList, RpcRequest rpcRequest);
}
