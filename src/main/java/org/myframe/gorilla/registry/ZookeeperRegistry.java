package org.myframe.gorilla.registry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.myframe.gorilla.cluster.Cluster;
import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.rpc.Provider;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.utils.LoggerUtil;
import org.myframe.gorilla.utils.ZkUtils;

public class ZookeeperRegistry implements Registry {

	private CuratorFramework curatorFramework;
	private ConcurrentHashMap<String, PathChildrenCache> watcher = new ConcurrentHashMap<>();

	private final ReentrantLock clientLock = new ReentrantLock();
	private final ReentrantLock serverLock = new ReentrantLock();

	public ZookeeperRegistry(CuratorFramework curatorFramework) {
		this.curatorFramework = curatorFramework;
	}

	@Override
	public synchronized void publishService(Provider<?> provider) throws Exception {
		try {
			serverLock.lock();

			print(">>>>>注册服务:", provider.getInterface().getName());

			//
			checkRootPath();
			//
			String addr = provider.getURL().getAddr();
			String suAddr = provider.getURL().getSuAddr();
			regInterface(suAddr);
			regNodeInfo(addr, provider.getURL());
			// watcher self
			curatorFramework.getData().usingWatcher(new CuratorWatcher() {

				@Override
				public void process(WatchedEvent event) throws Exception {
					LoggerUtil.error(event.getPath() + "节点变动.... 变动类型：" + event.getType().getIntValue());

					if (event.getType().getIntValue() == Watcher.Event.EventType.NodeDeleted.getIntValue()) {
						publishService(provider);
					}
				}
			}).forPath(addr);
		} finally {
			serverLock.unlock();
		}
	}

	private void checkRootPath() throws Exception {
		Stat stat = curatorFramework.checkExists().forPath(GorillaConstants.ZK_ROOT_PATH);
		if (null == stat) {
			curatorFramework.create().creatingParentContainersIfNeeded().forPath(GorillaConstants.ZK_ROOT_PATH);
		}
	}

	private void regInterface(String interf) throws Exception {
		Stat stat = curatorFramework.checkExists().forPath(interf);
		if (null == stat) {
			curatorFramework.create().creatingParentContainersIfNeeded().forPath(interf);
		}
	}

	private void regNodeInfo(String addr, URL url) throws Exception {
		Stat stat = curatorFramework.checkExists().forPath(addr);
		if (null != stat) {
			curatorFramework.delete().forPath(addr);
		}
		curatorFramework.create().withMode(CreateMode.EPHEMERAL).forPath(addr, ZkUtils.toBytes(url.getValue()));

	}

	@Override
	public void subscribeService(Class<?> clz, Cluster<?> cluster) throws Exception {
		try {
			clientLock.lock();
			//
			String key = clz.getName();
			if (watcher.containsKey(key)) {
				return;
			}

			print("订阅接口", clz.getName());

			PathChildrenCache cached = new PathChildrenCache(curatorFramework, ZkUtils.toPath(key), true);
			cached.getListenable().addListener(new ZkPathChildrenListener(cached, cluster, clz));
			//
			cached.start();
			watcher.put(key, cached);

		} finally {
			clientLock.unlock();
		}

	}

	private void print(String type, String interfaceName) {
		LoggerUtil.error("---------------------------------");
		LoggerUtil.error(type + interfaceName);
		LoggerUtil.error("---------------------------------");
	}

}
