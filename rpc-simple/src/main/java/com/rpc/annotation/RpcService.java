package com.rpc.annotation;

import java.lang.annotation.*;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcService.java
 * @Description: 用于暴露服务接口
 * @CreateTime: 2022/7/28 14:51:00
 **/
@Target(ElementType.TYPE)   // 用于类上
@Retention(RetentionPolicy.RUNTIME) // 在运行时获取到
@Inherited
public @interface RpcService {
}
