package com.rpc.spring;

import com.rpc.annotation.RpcReference;
import com.rpc.annotation.RpcScan;
import com.rpc.annotation.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.stereotype.Component;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: CustomScannerRegistry.java
 * @Description: scan and filter specified annotations
 * @CreateTime: 2022/8/8 17:07:00
 **/
public class CustomScannerRegistry implements ImportBeanDefinitionRegistrar, ResourceLoaderAware {
    private final Logger logger = LoggerFactory.getLogger(CustomScannerRegistry.class);

    private static final String SPRING_BEAN_BASE_PACKAGE = "com.rpc";
    private static final String BASE_PACKAGE_ATTRIBUTE_NAME = "basePackage";
    private ResourceLoader resourceLoader;

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry registry) {
        AnnotationAttributes rpcScanAnnotationAttributes = AnnotationAttributes.fromMap(annotationMetadata.getAnnotationAttributes(RpcScan.class.getName()));
        String[] rpcScanBasePackages = new String[0];
        if (rpcScanBasePackages != null) {
            rpcScanBasePackages = rpcScanAnnotationAttributes.getStringArray(BASE_PACKAGE_ATTRIBUTE_NAME);
        }
        if (rpcScanBasePackages.length == 0) {
            rpcScanBasePackages = new String[] {((StandardAnnotationMetadata)annotationMetadata).getIntrospectedClass().getPackage().getName()};
        }
        // Scan the RpcService annotation
        CustomScanner rpcServiceScanner = new CustomScanner(registry, RpcService.class);
        // Scan the Component annotation
        CustomScanner springBeanScanner = new CustomScanner(registry, Component.class);

        CustomScanner rpcReference = new CustomScanner(registry, RpcReference.class);

        if (resourceLoader != null) {
            rpcServiceScanner.setResourceLoader(resourceLoader);
            springBeanScanner.setResourceLoader(resourceLoader);
        }

        int springBeanAmount = springBeanScanner.scan(SPRING_BEAN_BASE_PACKAGE);
        logger.info("springBeanScanner 扫描的数量 [{}]", springBeanAmount);

        int rpcServiceCount = rpcServiceScanner.scan(rpcScanBasePackages);
        logger.info("rpcServiceCount 扫描的数量 [{}]", rpcServiceCount);
    }
}
