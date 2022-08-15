package com.rpc.remoting.transport.netty.client;

import com.rpc.extension.ExtensionLoader;
import com.rpc.registry.ServiceDiscovery;
import com.rpc.remoting.dto.RpcRequest;
import com.rpc.remoting.dto.RpcResponse;
import com.rpc.remoting.transport.RpcRequestTransport;
import com.rpc.remoting.transport.netty.server.NettyRpcServer;
import com.rpc.remoting.transport.netty.server.NettyRpcServerHandler;
import com.rpc.serialize.Serializer;
import com.rpc.serialize.hessian.HessianSerializer;
import com.rpc.utils.SingletonFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.bytes.ByteArrayDecoder;
import io.netty.handler.codec.bytes.ByteArrayEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: NettyRpcClient.java
 * @Description: initialize and close Bootstrap object
 * @CreateTime: 2022/8/5 16:30:00
 **/
public class NettyRpcClient implements RpcRequestTransport {

    Logger logger = LoggerFactory.getLogger(NettyRpcClient.class);

    private final ServiceDiscovery serviceDiscovery;
    private final ChannelProvider channelProvider;
    private final Bootstrap bootstrap;
    private final EventLoopGroup eventLoopGroup;
    private final Serializer serializer;

    public NettyRpcClient() {
        eventLoopGroup = new NioEventLoopGroup();
        bootstrap = new Bootstrap();
        bootstrap.group(eventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new IdleStateHandler(0, 5, 0, TimeUnit.SECONDS));
                        socketChannel.pipeline().addLast(new ByteArrayEncoder());
                        socketChannel.pipeline().addLast(new ByteArrayDecoder());
                        // 添加自定义处理器
                        socketChannel.pipeline().addLast(new NettyRpcClientHandler());
                    }
                });

        this.serviceDiscovery = ExtensionLoader.getExtensionLoader(ServiceDiscovery.class).getExtension("redis");
        this.channelProvider = SingletonFactory.getInstance(ChannelProvider.class);
        this.serializer = ExtensionLoader.getExtensionLoader(Serializer.class).getExtension("serializer");
    }

    /**
     * connect server and get the channel ,so that you can send rpc message to server
     * @param inetSocketAddress server address
     * @return the channel
     */
    public Channel doConnect(InetSocketAddress inetSocketAddress) {
        CompletableFuture<Channel> completableFuture = new CompletableFuture<>();
        bootstrap.connect(inetSocketAddress).addListener((ChannelFutureListener) future -> {
           if (future.isSuccess()) {
               logger.info("The client has connected [{}] successful", inetSocketAddress);
               completableFuture.complete(future.channel());
           } else {
               throw new IllegalStateException();
           }
        });
        try {
            return completableFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("get the channel fail");
        }
    }

    @Override
    public Object sendRpcRequest(RpcRequest rpcRequest) {
        CompletableFuture<RpcResponse> resultFuture = new CompletableFuture<>();
        // get the server address
        InetSocketAddress inetSocketAddress = serviceDiscovery.lookupService(rpcRequest);

        logger.info("服务发现：[{}]", inetSocketAddress.toString());

        // through the server address related the channel
        Channel channel = getChannel(inetSocketAddress);
        if (channel.isActive()) {
            byte[] bytes = this.serializer.serializer(rpcRequest);
            channel.writeAndFlush(bytes).addListener((ChannelFutureListener) future ->{
                if (future.isSuccess()) {
                    logger.info("client send message: [{}]", rpcRequest.getClassName());
                } else {
                    future.channel().close();
                    resultFuture.completeExceptionally(future.cause());
                    logger.error("Send failed: ", future.cause());
                }
            });
        } else {
            throw new IllegalStateException();
        }

        return resultFuture;
    }

    public Channel getChannel(InetSocketAddress inetSocketAddress) {
        Channel channel = channelProvider.get(inetSocketAddress);
        if (channel == null) {
            channel = doConnect(inetSocketAddress);
            channelProvider.set(inetSocketAddress, channel);
        }

        return channel;
    }

    public void close() {
        eventLoopGroup.shutdownGracefully();
    }
}
