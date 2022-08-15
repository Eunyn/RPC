package com.rpc.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySources;
import redis.clients.jedis.Jedis;

import java.lang.annotation.Annotation;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RedisUtil.java
 * @Description: redis 用作服务注册
 * @CreateTime: 2022/8/1 16:03:00
 **/

public class RedisUtil {

    @Value("spring.redis.host")
    private String HOST;

    private Integer port;

    private static final Jedis jedis = new Jedis("192.168.3.147", 6379);

    private static final Set<String> interfaceIpLists = new HashSet<>();
    private static final Map<String, Set<String>> interfacesMap = new ConcurrentHashMap<>();



    /**
     * 服务注册
     * @param key 服务名称
     * @param value 服务对象
     */
    public static void serviceRegistry(String key, String value) {
//        System.out.println("Redis 服务注册{" + key + "=" + value + "}");
        if (interfacesMap.containsKey(key)) {
            interfacesMap.get(key).add(value);
        } else {
            Set<String> ip = new HashSet<>();
            ip.add(value);
            interfacesMap.put(key, ip);
        }

        jedis.sadd(key, value);
    }

    /**
     * 获取服务
     * @param key 服务名称
     * @return 服务对象
     */
    public static Set<String> serviceDiscovery(String key) {
        //        System.out.println("Redis 服务发现{" + key + "=" + value + "}");
        if (key == null || key.length() == 0)
            return null;

        if (interfacesMap.containsKey(key))
            return interfacesMap.get(key);

        return jedis.smembers(key);
    }
}
