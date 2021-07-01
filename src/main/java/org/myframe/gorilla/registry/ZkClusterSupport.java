package org.myframe.gorilla.registry;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.myframe.gorilla.cluster.Cluster;
import org.myframe.gorilla.cluster.ClusterSupport;
import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.extension.SpiMeta;
import org.myframe.gorilla.rpc.DefaultReferer;
import org.myframe.gorilla.rpc.Referer;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.transport.Client;
import org.myframe.gorilla.transport.EndpointFactory;
import org.myframe.gorilla.utils.ZkUtils;

@SpiMeta(name = GorillaConstants.DEFAULT_VALUE)
public class ZkClusterSupport<T> implements ClusterSupport<T> {

	private ConcurrentHashMap<String, List<Referer<T>>> refs = new ConcurrentHashMap<>();

	private List<Referer<T>> getRef(String clzName) {
		List<Referer<T>> list = null;
		if (!refs.containsKey(clzName)) {

			list = new ArrayList<>();
			refs.putIfAbsent(clzName, list);
		}

		list = refs.get(clzName);
		return list;
	}

	@Override
	public void child_added(byte[] data, Cluster<T> cluster, Class<T> clazz) throws Exception {
		//
		List<Referer<T>> list = getRef(clazz.getName());
		//
		String source = ZkUtils.toString(data);

		// check has same one
		boolean has = false;
		for (Referer<T> r : list) {
			if (r.getUrl().getValue().equals(source)) {
				has = true;
			}
		}
		if (has) {
			return;
		}

		URL url = new URL(ZkUtils.getAddr(source), ZkUtils.getPort(source), clazz.getName());
		//
		EndpointFactory factory = ExtensionLoader.getExtensionLoader(EndpointFactory.class)
				.getExtension(GorillaConstants.DEFAULT_VALUE);
		Client client = factory.createClient(url);

		Referer<T> referer = new DefaultReferer<>(client, cluster.getInterface(), factory);

		list.add(referer);

		//
		cluster.onRefresh(list);
		cluster.init();
	}

	@Override
	public void child_updated(byte[] data, Cluster<T> cluster, Class<T> clazz) throws Exception {

	}

	@Override
	public void child_removed(byte[] data, Cluster<T> cluster, Class<T> clazz) throws Exception {
		//
		List<Referer<T>> list = getRef(clazz.getName());
		//
		String source = ZkUtils.toString(data);
		URL url = new URL(ZkUtils.getAddr(source), ZkUtils.getPort(source), clazz.getName());

		Iterator<Referer<T>> iterator = list.iterator();

		while (iterator.hasNext()) {
			Referer<T> ref = iterator.next();
			if (ref.getUrl().getHost().equals(url.getHost()) && ref.getUrl().getPort() == url.getPort()) {
				ref.destroy();
				iterator.remove();
			}
		}

		//
		cluster.onRefresh(list);
		cluster.init();
	}

}
