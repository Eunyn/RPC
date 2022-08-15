package com.rpc.registry.redis;

import com.rpc.extension.ExtensionLoader;
import com.rpc.loadbalance.LoadBalance;
import com.rpc.registry.ServiceDiscovery;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.utils.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RedisServiceDiscoveryImpl.java
 * @Description: redis 服务发现实现类
 * @CreateTime: 2022/7/31 14:38:00
 **/
public class RedisServiceDiscoveryImpl implements ServiceDiscovery {

    private final Logger logger = LoggerFactory.getLogger(RedisServiceDiscoveryImpl.class);
    private final LoadBalance loadBalance;

    public RedisServiceDiscoveryImpl() {
        loadBalance = ExtensionLoader.getExtensionLoader(LoadBalance.class).getExtension("loadBalance");
    }

    @Override
    public InetSocketAddress lookupService(RpcRequest rpcRequest) {
        String rpcServiceName = rpcRequest.getClassName();

        Set<String> address = RedisUtil.serviceDiscovery(rpcServiceName);
        List<String> lists = new ArrayList<>(address);
        String s = loadBalance.selectServiceAddress(lists, rpcRequest);

        logger.info("the node of the select is [{}]", s);

        String[] ipPort = s.split(":");
        String host = ipPort[0].substring(1);
        int port = Integer.parseInt(ipPort[1]);

        return new InetSocketAddress(host, port);
    }
}
