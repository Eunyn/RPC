package com.rpc.config;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcServiceConfig.java
 * @Description: RPC 服务配置
 * @CreateTime: 2022/7/29 17:18:00
 **/
public class RpcServiceConfig {

    /**
     * 服务版本号
     */
    private String version = "";

    /**
     * 服务组
     */
    private String group = "";

    /**
     * 目标服务
     */
    private Object service;

    // 获取实现类名
    public Object getServiceImplName() {
        return this.service;
    }

    // 获取接口名
    public String getServiceName() {
        return this.service.getClass().getInterfaces()[0].getCanonicalName();
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public Object getService() {
        return service;
    }

    public void setService(Object service) {
        this.service = service;
    }
}
