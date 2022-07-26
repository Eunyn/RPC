package com.rpc.pojo;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: User.java
 * @Description: 实体类
 * @CreateTime: 2022/7/24 17:35:00
 **/
public class User {
    private Integer id;

    private String name;

    public User() {}

    public User(Integer id) {
        this.id = id;
    }

    public User(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
