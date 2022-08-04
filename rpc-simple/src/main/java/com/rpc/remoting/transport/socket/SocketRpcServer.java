package com.rpc.remoting.transport.socket;

import com.rpc.config.RpcServiceConfig;
import com.rpc.provider.ServiceProvider;
import com.rpc.provider.impl.RedisServiceProviderImpl;
import com.rpc.utils.SingletonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: SocketRpcServer.java
 * @Description: 服务端
 * @CreateTime: 2022/8/2 16:38:00
 **/
public class SocketRpcServer {

    private final Logger logger = LoggerFactory.getLogger(SocketRpcServer.class);
    private final ExecutorService threadPool;
    private final ServiceProvider serviceProvider;

    public SocketRpcServer() {
        threadPool = Executors.newCachedThreadPool();
        serviceProvider = SingletonFactory.getInstance(RedisServiceProviderImpl.class);
    }

    public void registerService(RpcServiceConfig rpcServiceConfig) {
        serviceProvider.publishService(rpcServiceConfig);
    }

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket()){
            String host = InetAddress.getLocalHost().getHostAddress();
            serverSocket.bind(new InetSocketAddress(host, 8989));

            Socket socket;
            while ((socket = serverSocket.accept()) != null) {
                logger.info("client connected [{}]", socket.getInetAddress());
                threadPool.execute(new SocketRpcRequestHandlerRunnable(socket));
            }
            threadPool.shutdown();
        } catch (IOException e) {
            logger.error("occur IOException: ", e);
        }
    }
}
