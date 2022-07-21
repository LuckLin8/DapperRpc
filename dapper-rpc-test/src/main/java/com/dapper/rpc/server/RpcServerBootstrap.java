package com.dapper.rpc.server;

import com.dapper.rpc.serializer.core.NettyServer;
import com.dapper.rpc.service.HelloService;
import com.dapper.rpc.service.HelloServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcServerBootstrap {
    private static final Logger logger = LoggerFactory.getLogger(RpcServerBootstrap.class);

    public static void main(String[] args) {
        String serverAddress = "127.0.0.1:18877";
        String registryAddress = "127.0.0.1:2181";
        NettyServer nettyRpcServer = new NettyServer(serverAddress, registryAddress);
        HelloService helloService1 = new HelloServiceImpl();
        nettyRpcServer.addService(HelloService.class.getName(), "1.0", helloService1);
        try {
            nettyRpcServer.start();
        } catch (Exception ex) {
            logger.error("Exception: {}", ex.toString());
        }
    }
}
