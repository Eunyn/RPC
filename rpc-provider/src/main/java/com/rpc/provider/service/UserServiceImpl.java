package com.rpc.provider.service;

import com.rpc.annotation.RpcService;
import com.rpc.api.IUserService;
import com.rpc.pojo.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: UserServiceImpl.java
 * @Description: 实现类
 * @CreateTime: 2022/7/24 17:46:00
 **/
@RpcService
@Service
public class UserServiceImpl implements IUserService {

    private final Map<Object, User> userMap = new HashMap<>();

    @Override
    public User getById(int id) {
        if (userMap.size() == 0) {
            User user1 = new User(1, "张三");
            User user2 = new User(2, "李四");
            userMap.put(user1.getId(), user1);
            userMap.put(user2.getId(), user2);
        }
        if (userMap.get(id) == null)
            return new User(-1, "error");

        return userMap.get(id);
    }
}
