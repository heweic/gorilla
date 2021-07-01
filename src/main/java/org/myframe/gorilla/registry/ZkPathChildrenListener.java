package org.myframe.gorilla.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.myframe.gorilla.cluster.Cluster;
import org.myframe.gorilla.cluster.ClusterSupport;
import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.utils.LoggerUtil;

public class ZkPathChildrenListener<T> implements PathChildrenCacheListener, NotifListener<T> {

	private PathChildrenCache cached;
	private Cluster<T> cluster;
	private ClusterSupport<T> clusterSupport;

	private Class<T> clazz;

	@SuppressWarnings("unchecked")
	public ZkPathChildrenListener(PathChildrenCache cached, Cluster<T> cluster, Class<T> interfaceClass) {
		this.cached = cached;
		this.cluster = cluster;
		this.clusterSupport = ExtensionLoader.getExtensionLoader(ClusterSupport.class)
				.getExtension(GorillaConstants.DEFAULT_VALUE);
		this.clazz = interfaceClass;
	}

	@Override
	public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
		PathChildrenCacheEvent.Type eventType = event.getType();

		switch (eventType) {
		case CONNECTION_RECONNECTED:
			cached.rebuild();
			break;
		case CONNECTION_SUSPENDED:

			break;
		case CONNECTION_LOST:
			break;
		case CHILD_ADDED:
			print(">>>>>>添加节点:", event);
			clusterSupport.child_added(event.getData().getData(), cluster, clazz);
			break;
		case CHILD_UPDATED:
			print(">>>>>>更新节点:", event);
			break;
		case CHILD_REMOVED:
			print(">>>>>>删除节点:", event);
			clusterSupport.child_removed(event.getData().getData(), cluster, clazz);
			break;
		default:
			//
			return;
		// URL url = new URL(host, port, interfaceName);
		// endpointFactory.createClient(url);
		}
	}

	private void print(String type, PathChildrenCacheEvent event) {
		LoggerUtil.error(type + event.getData().getPath());
	}

}
