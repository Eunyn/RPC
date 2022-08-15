package com.rpc.spring;

import com.rpc.annotation.RpcReference;
import com.rpc.annotation.RpcService;
import com.rpc.config.RpcServiceConfig;
import com.rpc.extension.ExtensionLoader;
import com.rpc.provider.ServiceProvider;
import com.rpc.provider.impl.RedisServiceProviderImpl;
import com.rpc.proxy.RpcClientProxy;
import com.rpc.remoting.transport.RpcRequestTransport;
import com.rpc.utils.SingletonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: SpringBeanPostProcessor.java
 * @Description: TODO
 * @CreateTime: 2022/8/3 16:46:00
 **/
@Component
public class SpringBeanPostProcessor implements BeanPostProcessor {

    Logger logger = LoggerFactory.getLogger(SpringBeanPostProcessor.class);

    private final ServiceProvider serviceProvider;
    private final RpcRequestTransport rpcClient;

    public SpringBeanPostProcessor() {
        this.serviceProvider = SingletonFactory.getInstance(RedisServiceProviderImpl.class);
        this.rpcClient = ExtensionLoader.getExtensionLoader(RpcRequestTransport.class).getExtension("netty");
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean.getClass().isAnnotationPresent(RpcService.class)) {
            logger.info("[{}] is annotated with [{}]", bean.getClass().getName(), RpcService.class.getCanonicalName());
            // get RpcService annotation
            RpcService rpcService = bean.getClass().getAnnotation(RpcService.class);
            RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();
            // build RpcServiceProperties
            rpcServiceConfig.setService(bean);
            serviceProvider.publishService(rpcServiceConfig);
        }

        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> targetClass = bean.getClass();
        Field[] declaredFields = targetClass.getDeclaredFields();
        for (Field field : declaredFields) {
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference != null) {
                RpcServiceConfig rpcServiceConfig = new RpcServiceConfig();

                rpcServiceConfig.setService(rpcReference.getClass());

                RpcClientProxy rpcClientProxy = new RpcClientProxy(rpcClient, rpcServiceConfig);
                Object proxy = rpcClientProxy.getProxy(field.getType());
                try {
                    // 4.属性注入
                    field.setAccessible(true);
                    field.set(bean, proxy);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }

        return bean;
    }
}
