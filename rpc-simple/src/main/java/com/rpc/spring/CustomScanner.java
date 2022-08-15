package com.rpc.spring;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.lang.annotation.Annotation;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: CustomScanner.java
 * @Description: custom package scanner
 * @CreateTime: 2022/8/8 17:19:00
 **/
public class CustomScanner extends ClassPathBeanDefinitionScanner {
    public CustomScanner(BeanDefinitionRegistry registry, Class<? extends Annotation> annoType) {
        super(registry);
        super.addIncludeFilter(new AnnotationTypeFilter(annoType));
    }

    @Override
    public int scan(String... basePackages) {
        return super.scan(basePackages);
    }
}
