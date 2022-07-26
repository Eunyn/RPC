package com.rpc.provider.handler;

import com.alibaba.fastjson.JSON;
import com.rpc.common.RpcRequest;
import com.rpc.common.RpcResponse;
import com.rpc.provider.anno.RpcService;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.cglib.reflect.FastClass;
import org.springframework.cglib.reflect.FastMethod;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyServerHandler.java
 * @Description: 自定义业务处理类
 *                  1. 将标有 @RpcService 的注解的 bean 进行缓存
 *                  2. 接收客户端请求
 *                  3. 根据传递过来的 beanName 从缓存中查找
 *                  4. 通过反射调用 bean 方法
 *                  5. 给客户端响应
 * @CreateTime: 2022/7/25 15:18:00
 **/
@Component
@ChannelHandler.Sharable    // 设置通道共享
public class NettyServerHandler extends SimpleChannelInboundHandler<String> implements ApplicationContextAware {

    private final static Map<String, Object> SERVICE_INSTANCE_MAP = new HashMap<>();
    private final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);
    
    /**
     * 1. 将标有 @RpcService 的注解的 bean 进行缓存
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        // 1.1 通过注解获取 bean 集合
        Map<String, Object> serviceMap = applicationContext.getBeansWithAnnotation(RpcService.class);
        // 1.2 循环遍历
        for (Map.Entry<String, Object> entry : serviceMap.entrySet()) {
            Object serviceBean = entry.getValue();
            if (serviceBean.getClass().getInterfaces().length == 0) {
                throw new RuntimeException("对外暴露的服务必须实现接口");
            }

            // 默认处理第一个作为缓存 bean 的名字
            String serviceName = serviceBean.getClass().getInterfaces()[0].getName();
            SERVICE_INSTANCE_MAP.put(serviceName, serviceBean);
            logger.info("{}", SERVICE_INSTANCE_MAP);
        }
    }

    /**
     * 通道读取就绪事件 —— 读取客户端消息
     * @param ctx
     * @param s
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        // 2. 接收客户端请求
        RpcRequest rpcRequest = JSON.parseObject(s, RpcRequest.class);
        RpcResponse rpcResponse = new RpcResponse();
        rpcResponse.setRequestId(rpcRequest.getRequestId());

        logger.debug("客户端请求 --> 类名: {}", rpcRequest.getClassName());
        logger.debug("客户端请求 --> 参数: {}", rpcRequest.getParameters());

        // 业务处理
        try {
            rpcResponse.setResult(handler(rpcRequest));
        } catch (Exception e) {
            e.printStackTrace();
            rpcResponse.setError(e.getMessage());
        }


        // 5. 给客户端响应
        ctx.writeAndFlush(JSON.toJSONString(rpcResponse));
    }

    private Object handler(RpcRequest rpcRequest) throws InvocationTargetException {
        // 3. 根据传递过来的 beanName 从缓存中查找
        Object serviceBean = SERVICE_INSTANCE_MAP.get(rpcRequest.getClassName());
        if (serviceBean == null) {
            throw new RuntimeException("服务端没有找到服务");
        }

        // 4. 通过反射调用 bean 方法
        FastClass proxyClass = FastClass.create(serviceBean.getClass());
        FastMethod method = proxyClass.getMethod(rpcRequest.getMethodName(), rpcRequest.getParameterTypes());

        return method.invoke(serviceBean, rpcRequest.getParameters());
    }
}
