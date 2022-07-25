package com.rpc.provider.service;

import com.rpc.api.IUserService;
import com.rpc.pojo.User;
import com.rpc.provider.anno.RpcService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: UserServiceImpl.java
 * @Description: TODO
 * @CreateTime: 2022/7/24 17:46:00
 **/
@RpcService
@Service
public class UserServiceImpl implements IUserService {

    private final Map<Object, User> userMap = new HashMap<>();

    @Override
    public User getById(Integer id) {
        if (userMap.size() == 0) {
            User user1 = new User(1, "张三");
            User user2 = new User(2, "李四");
            userMap.put(user1.getId(), user1);
            userMap.put(user2.getId(), user2);
        }

        return userMap.get(id);
    }
}
