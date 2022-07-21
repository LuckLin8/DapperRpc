package com.dapper.rpc.serializer.core;

import com.dapper.rpc.serializer.registry.ServiceRegistry;
import com.dapper.rpc.utils.ServiceUtil;
import com.dapper.rpc.utils.ThreadPoolUtil;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class NettyServer extends Server {

    private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

    //注册的service集合
    private final Map<String, Object> registryServiceMap = new HashMap<>();
    private final ServiceRegistry serviceRegistry;
    private final String serverAddress;

    public NettyServer(String serverAddress, String registryAddress) {
        this.serverAddress = serverAddress;
        this.serviceRegistry = new ServiceRegistry(registryAddress);
    }

    public void addService(String interfaceName, String version, Object serviceBean) {
        logger.info("Adding service, interface: {}, version: {}, bean：{}", interfaceName, version, serviceBean);
        String serviceKey = ServiceUtil.makeServiceKey(interfaceName, version);
        registryServiceMap.put(serviceKey, serviceBean);
    }

    @Override
    public void start() {
        ThreadPoolUtil.SERVER_START_THREAD_POOL.execute(()->{
            EventLoopGroup bossGroup = new NioEventLoopGroup();
            EventLoopGroup workerGroup = new NioEventLoopGroup();
            try {
                ServerBootstrap bootstrap = new ServerBootstrap();
                bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
                        .childHandler(new RpcServerInitializer(registryServiceMap, ThreadPoolUtil.NETTY_SERVER_THREAD_POOL))
                        .option(ChannelOption.SO_BACKLOG, 128)
                        .childOption(ChannelOption.SO_KEEPALIVE, true);
                String[] array = serverAddress.split(":");
                String host = array[0];
                int port = Integer.parseInt(array[1]);
                ChannelFuture future = bootstrap.bind(host, port).sync();

                serviceRegistry.registerService(host, port, registryServiceMap);
                logger.info("Server started on port {}", port);
                future.channel().closeFuture().sync();
            } catch (InterruptedException e){
                logger.info("Rpc server remoting server stop");
                Thread.currentThread().interrupt();
            } catch (Exception ex){
                logger.error("Rpc server remoting server error", ex);
            } finally {
                try {
                    serviceRegistry.unregisterService();
                    workerGroup.shutdownGracefully();
                    bossGroup.shutdownGracefully();
                } catch (Exception ex) {
                    logger.error(ex.getMessage(), ex);
                }
            }
        });
    }

    @Override
    public void stop() {

    }
}
