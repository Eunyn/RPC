package com.rpc.registry.redis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisMonitor;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: CuratorUtils.java
 * @Description: Curator(redis client) utils
 * @CreateTime: 2022/8/2 15:32:00
 **/
public class CuratorUtils {
    private final Logger logger = LoggerFactory.getLogger(CuratorUtils.class);

    private static final int BASE_SLEEP_TIME = 1000;
    private static final int MAX_RETRIES = 3;
    public static final String REDIS_REGISTER_ROOT_PATH = "/my-rpc";
    private static final Map<String, List<String>> SERVICE_ADDRESS_MAP = new ConcurrentHashMap<>();
    private static final Set<String> REGISTERED_PATH_SET = ConcurrentHashMap.newKeySet();
//    private static JedisMonitor redisClient = ;
    private static final String DEFAULT_REDIS_ADDRESS = "192.168.3.147:6379";

    private CuratorUtils(){}

    public static void createPersistentNode(String path) {

    }
}
