package com.rpc.remoting.transport.netty.client;

import com.rpc.extension.ExtensionLoader;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.remoting.dto.RpcResponse;
import com.rpc.serialize.Serializer;
import com.rpc.utils.SingletonFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.ReferenceCountUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyRpcClientHandler.java
 * @Description: Customize the client ChannelHandler to process the data sent by the server
 * @CreateTime: 2022/8/5 16:37:00
 **/
public class NettyRpcClientHandler extends ChannelInboundHandlerAdapter {
    private final Logger logger = LoggerFactory.getLogger(NettyRpcClientHandler.class);
    private final NettyRpcClient nettyRpcClient;
    private final Serializer serializer;

    public NettyRpcClientHandler() {
        this.nettyRpcClient = SingletonFactory.getInstance(NettyRpcClient.class);
        serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension("serializer");
    }

    /**
     * Read the message transmitted by the server
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            byte[] response = (byte[])msg;
            RpcResponse rpcResponse = serializer.deserializer(response, RpcResponse.class);
            if (rpcResponse.getResult() != null) {
                logger.info("heart [{}]", rpcResponse.getResult());
            } else {
                logger.error("error [{}]", rpcResponse.getError());
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

//    @Override
//    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
//        if (evt instanceof IdleStateEvent) {
//            IdleState state = ((IdleStateEvent) evt).state();
//            if (state == IdleState.WRITER_IDLE) {
//                logger.info("write idle happen [{}]", ctx.channel().remoteAddress());
//                Channel channel = nettyRpcClient.getChannel((InetSocketAddress) ctx.channel().remoteAddress());
//                RpcRequest rpcRequest = new RpcRequest();
//                rpcRequest.setRequestId(UUID.randomUUID().toString());
//                rpcRequest.setClassName("com.rpc.api.IUserService");
//                rpcRequest.setMethodName("getById");
//                rpcRequest.setParameters(new Integer[]{3});
//                rpcRequest.setParameterTypes(new Class[]{Integer.class});
//                byte[] bytes = this.serializer.serializer(rpcRequest);
//
//                channel.writeAndFlush(bytes).addListener(ChannelFutureListener.CLOSE_ON_FAILURE);
//            }
//        } else {
//            super.userEventTriggered(ctx, evt);
//        }
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("client catch exception: ", cause);
        cause.printStackTrace();
        ctx.close();
    }
}
