package com.rpc.remoting.transport.netty.server;

import com.rpc.extension.ExtensionLoader;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.remoting.dto.RpcResponse;
import com.rpc.remoting.handler.RpcRequestHandler;
import com.rpc.serialize.Serializer;
import com.rpc.utils.SingletonFactory;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyRpcServerHandler.java
 * @Description: Customize the ChannelHandler of the server to process the data sent by the client.
 * @CreateTime: 2022/8/5 15:54:00
 **/
public class NettyRpcServerHandler extends ChannelInboundHandlerAdapter{

    private final Logger logger = LoggerFactory.getLogger(NettyRpcServerHandler.class);

    private final RpcRequestHandler rpcRequestHandler;

    private final Serializer serializer;

    public NettyRpcServerHandler() {
        this.rpcRequestHandler = SingletonFactory.getInstance(RpcRequestHandler.class);
        this.serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension("serializer");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            byte[] bytes = (byte[]) msg;
            RpcRequest rpcRequest = serializer.deserializer(bytes, RpcRequest.class);
            // Execute the target method (the method the client needs to execute) and return the method result
            Object result = rpcRequestHandler.handle(rpcRequest);
            logger.info(String.format("server get result: %s", result.toString()));

            RpcResponse rpcResponse = new RpcResponse();
            if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                rpcResponse.setRequestId(rpcRequest.getRequestId());
                rpcResponse.setResult(result);
            } else {
                rpcResponse.setRequestId(rpcRequest.getRequestId());
                rpcResponse.setError("not writable now, message dropped");
                logger.error("not writable now, message dropped");
            }
            byte[] response = this.serializer.serializer(rpcResponse);
            ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
        } finally {
            // Ensure that ByteBuf is released, otherwise there may be memory leaks
            ReferenceCountUtil.release(msg);
        }
    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.READER_IDLE) {
//                logger.info("idle check happen, so close the connection");
//                ctx.close();
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("server catch exception");
        cause.printStackTrace();
        ctx.close();
    }
}
