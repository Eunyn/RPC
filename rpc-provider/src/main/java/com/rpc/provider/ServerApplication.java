package com.rpc.provider;

import com.rpc.provider.server.NettyRpcServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @Author: Eun
 * @Version 1.0.0
 * @ClassName: ServerApplication.java
 * @Description: mainServer
 * @CreateTime: 2022/7/24 17:34:00
 **/

@SpringBootApplication
public class ServerApplication implements CommandLineRunner {

    @Autowired
    NettyRpcServer rpcServer;

    public static void main(String[] args) {
        SpringApplication.run(ServerApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                rpcServer.start("localhost", 8899);
            }
        }).start();
    }
}
