package com.rpc.registry.zk;

import com.rpc.registry.ServiceDiscovery;
import com.rpc.remoting.dto.RpcRequest;

import java.net.InetSocketAddress;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: ZkServiceDiscoveryImpl.java
 * @Description: zookeeper 服务发现实现类
 * @CreateTime: 2022/7/31 16:49:00
 **/
public class ZkServiceDiscoveryImpl implements ServiceDiscovery {
    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        return null;
    }
}
