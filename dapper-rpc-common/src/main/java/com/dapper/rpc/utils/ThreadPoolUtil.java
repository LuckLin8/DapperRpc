package com.dapper.rpc.utils;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author bw.lin
 */
public class ThreadPoolUtil {
    public static final ThreadPoolExecutor NETTY_SERVER_THREAD_POOL = ThreadPoolUtil.makeServerThreadPool(
            "NettyServer", 16, 32);

    public static final ThreadPoolExecutor SERVER_START_THREAD_POOL = ThreadPoolUtil.makeServerThreadPool(
            "ServerStart", 1, 1);

    public static final ThreadPoolExecutor CLIENT_CALL_BACK_THREAD_POOL = ThreadPoolUtil.makeServerThreadPool(
            "ClientCallBack",16,16);

    private ThreadPoolUtil() {
        throw new IllegalStateException("Utility class");
    }

    private static ThreadPoolExecutor makeServerThreadPool(final String serviceName, int corePoolSize, int maxPoolSize) {
        return new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                60L,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                r -> new Thread(r, "netty-rpc-" + serviceName + "-" + r.hashCode()),
                new ThreadPoolExecutor.AbortPolicy());
    }
}
