package com.dapper.rpc.constant;

/**
 * @author bw.lin
 */
public class ZkConstant {
    public static final int ZK_SESSION_TIMEOUT = 5000;
    public static final int ZK_CONNECTION_TIMEOUT = 5000;

    public static final String ZK_REGISTRY_PATH = "/registry";
    public static final String ZK_DATA_PATH = ZK_REGISTRY_PATH + "/data";

    public static final String ZK_NAMESPACE = "dapper-rpc";
}
