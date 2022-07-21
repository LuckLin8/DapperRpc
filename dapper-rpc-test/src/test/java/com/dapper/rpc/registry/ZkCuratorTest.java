package com.dapper.rpc.registry;

import com.dapper.rpc.registry.zookeeper.CuratorClient;
import com.dapper.rpc.serializer.BaseSerializer;
import com.dapper.rpc.serializer.protostuff.ProtostuffSerializer;
import org.junit.Test;

public class ZkCuratorTest {
    private final CuratorClient client = CuratorClient.getClient("127.0.0.1:2181", 5000);


    /**
     * //创建一个有内容的节点数据
     *         client.create().forPath("/a","hello world".getBytes());
     *         //创建一个多级目录的节点
     *         client.create().creatingParentsIfNeeded().forPath("/b/b1/b2","good".getBytes());
     *         //创建一个带序号的持久节点 PERSISTENT_SEQUENTIAL:带序号的 相当于命令行的 -s
     *         client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT_SEQUENTIAL).forPath("/c/c1","niubi".getBytes());
     *         //创建临时节点 设置时延5秒关闭
     *         client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/d","nb".getBytes());
     *         //创建临时有序节点
     *         client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/e","6666".getBytes());
     * @throws Exception
     */
    @Test
    public void curatorClientTest() throws Exception {
        BaseSerializer serializer = new ProtostuffSerializer();
        final byte[] bytes = serializer.serialize("测试");
        //临时节点
        final String pathData = client.createPathData("/registry/test", bytes);
        System.out.println(pathData);
        client.close();
    }
}

