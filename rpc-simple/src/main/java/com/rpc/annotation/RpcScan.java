package com.rpc.annotation;

import com.rpc.spring.CustomScannerRegistry;
import com.rpc.spring.SpringBeanPostProcessor;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcScan.java
 * @Description: 扫描包
 * @CreateTime: 2022/7/31 14:47:00
 **/
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Import({CustomScannerRegistry.class})
@Documented
public @interface RpcScan {
    String[] basePackage();
}
