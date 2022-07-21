package com.dapper.rpc.codec;

public class Heartbeat {
    public static final int HEARTBEAT_INTERVAL = 30;
    public static final int HEARTBEAT_TIMEOUT = 3 * HEARTBEAT_INTERVAL;
    public static final String HEARTBEAT_ID = "BEAT_PING_PONG";

    public static RpcRequest HEARTBEAT_PING;

    static {
        HEARTBEAT_PING = new RpcRequest() {};
        HEARTBEAT_PING.setRequestId(HEARTBEAT_ID);
    }
}
