package com.rpc.annotation;

import java.lang.annotation.*;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcReference.java
 * @Description: 引用代理类
 * @CreateTime: 2022/7/28 14:53:00
 **/
@Target(ElementType.FIELD)  // 作用于字段
@Retention(RetentionPolicy.RUNTIME) // 在运行时可以获取到
@Inherited
public @interface RpcReference {
}
