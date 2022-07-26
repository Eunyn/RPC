package com.rpc.consumer.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyRpcClientHandler.java
 * @Description: 客户端业务处理
 * @CreateTime: 2022/7/25 16:50:00
 **/
@Component
public class NettyRpcClientHandler extends SimpleChannelInboundHandler<String> implements Callable {

    private ChannelHandlerContext context;
    private String requestMsg;  // 发送消息
    private String responseMsg; // 接收消息

    public void setRequestMsg(String requestMsg) {
        this.requestMsg = requestMsg;
    }

    /**
     * 通道读取事件 —— 读取服务端消息
     * @param channelHandlerContext
     * @param msg
     * @throws Exception
     */
    @Override
    protected synchronized void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
        responseMsg = msg;
        // 唤醒等待线程
        notify();
    }

    /**
     * 通道连接就绪事件
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        context = ctx;
    }


    /**
     * 给服务端发送消息
     * @return
     * @throws Exception
     */
    @Override
    public synchronized Object call() throws Exception {
        context.writeAndFlush(requestMsg);
        // 将线程处于等待状态
        wait();
        return responseMsg;
    }
}
