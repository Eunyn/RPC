package com.rpc.remoting.handler;

import com.rpc.provider.ServiceProvider;
import com.rpc.provider.impl.RedisServiceProviderImpl;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.utils.SingletonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: RpcRequestHandler.java
 * @Description: RpcRequest processor
 * @CreateTime: 2022/8/2 17:02:00
 **/
public class RpcRequestHandler {

    Logger logger = LoggerFactory.getLogger(RpcRequestHandler.class);

    private final ServiceProvider serviceProvider;

    public RpcRequestHandler() {
        serviceProvider = SingletonFactory.getInstance(RedisServiceProviderImpl.class);
    }

    /**
     * Processing rpcRequest: call the corresponding method, and then return the method
     * @param rpcRequest client request
     * @return 方法执行结果
     */
    public Object handle(RpcRequest rpcRequest) {
        Object service = serviceProvider.getService(rpcRequest.getClassName());
        logger.info("handle service: {}", service.getClass());
        logger.info("RpcRequest service: {}", rpcRequest.getClassName());
        return invokeTargetMethod(rpcRequest, service);
    }

    /**
     * get method execution results
     * @param rpcRequest client request
     * @param service service object
     * @return the result of the target method execution
     */
    private Object invokeTargetMethod(RpcRequest rpcRequest, Object service) {
        Object result = null;
        try {
            logger.info("方法名称：{}，方法参数类型：{}，参数值：{}", rpcRequest.getMethodName(), rpcRequest.getParameterTypes(), rpcRequest.getParameters());
//            Method method = service.getClass().getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
//            logger.info("method 名称：{}", method.getName());
//            result = method.invoke(service, rpcRequest.getParameters());

            FastClass proxyClass = FastClass.create(service.getClass());
            FastMethod method = proxyClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());
            result = method.invoke(service, rpcRequest.getParameters());
            logger.info("执行类：{}", service.getClass().getCanonicalName());
            logger.info("执行结果：{}", result);
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return result;
    }
}
