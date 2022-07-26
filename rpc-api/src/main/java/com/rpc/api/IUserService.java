package com.rpc.api;

import com.rpc.pojo.User;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: IUserService.java
 * @Description: 服务接口
 * @CreateTime: 2022/7/24 17:32:00
 **/
public interface IUserService {

    /**
     * 根据 id 查询用户
     *
     * @param id
     * @return
     */
    User getById(int id);
}
