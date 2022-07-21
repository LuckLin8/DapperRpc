package com.dapper.rpc.registry.zookeeper;

import com.dapper.rpc.constant.ZkConstant;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author bw.lin
 */
public class CuratorClient {
    private static final AtomicReference<CuratorClient> SINGLETON_CLIENT = new AtomicReference<>();
    private CuratorFramework client = null;

    private CuratorClient() {
    }

    public static CuratorClient getClient(String connectString, int timeout) {
        return getClient(connectString, ZkConstant.ZK_NAMESPACE, timeout, timeout);
    }

    public static CuratorClient getClient(String connectString) {
       return getClient(connectString, ZkConstant.ZK_NAMESPACE, ZkConstant.ZK_SESSION_TIMEOUT, ZkConstant.ZK_CONNECTION_TIMEOUT);
    }

    @SuppressWarnings("all")
    public static CuratorClient getClient(String connectString, String namespace, int sessionTimeout, int connectionTimeout) {
        CuratorClient curatorClient = null;
        while (true){
            curatorClient = SINGLETON_CLIENT.get();
            if (Objects.nonNull(curatorClient)){
                return curatorClient;
            }
            curatorClient = new CuratorClient();
            curatorClient.client = CuratorFrameworkFactory.builder().namespace(namespace).connectString(connectString)
                    .sessionTimeoutMs(sessionTimeout).connectionTimeoutMs(connectionTimeout)
                    .retryPolicy(new ExponentialBackoffRetry(1000, 10))
                    .build();
            curatorClient.client.start();
            SINGLETON_CLIENT.compareAndSet(null,curatorClient);
            return SINGLETON_CLIENT.get();
        }
    }

    public void addConnectionStateListener(ConnectionStateListener connectionStateListener) {
        client.getConnectionStateListenable().addListener(connectionStateListener);
    }

    public String createPathData(String path, byte[] data) throws Exception {
        return client.create().creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(path, data);
    }

    public void updatePathData(String path, byte[] data) throws Exception {
        client.setData().forPath(path, data);
    }

    public void deletePath(String path) throws Exception {
        client.delete().forPath(path);
    }

    public void watchNode(String path, Watcher watcher) throws Exception {
        client.getData().usingWatcher(watcher).forPath(path);
    }

    public byte[] getData(String path) throws Exception {
        return client.getData().forPath(path);
    }

    public List<String> getChildren(String path) throws Exception {
        return client.getChildren().forPath(path);
    }

    public void watchTreeNode(String path, TreeCacheListener listener) {
        TreeCache treeCache = new TreeCache(client, path);
        treeCache.getListenable().addListener(listener);
    }

    public void watchPathChildrenNode(String path, PathChildrenCacheListener listener) throws Exception {
        PathChildrenCache pathChildrenCache = new PathChildrenCache(client, path, true);
        //BUILD_INITIAL_CACHE 代表使用同步的方式进行缓存初始化。
        pathChildrenCache.start(PathChildrenCache.StartMode.BUILD_INITIAL_CACHE);
        pathChildrenCache.getListenable().addListener(listener);
    }

    public void close() {
        client.close();
    }
}
