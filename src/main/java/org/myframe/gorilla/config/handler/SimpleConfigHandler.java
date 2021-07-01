package org.myframe.gorilla.config.handler;

import org.myframe.gorilla.cluster.Cluster;
import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.extension.SpiMeta;
import org.myframe.gorilla.proxy.ProxyFactory;
import org.myframe.gorilla.proxy.RefererInvocationHandler;

@SpiMeta(name = GorillaConstants.DEFAULT_VALUE)
public class SimpleConfigHandler implements ConfigHandler {

	@Override
	public <T> T refer(Class<T> interfaceClass, Cluster<T> cluster, String proxyName) {
		ProxyFactory proxyFactory = ExtensionLoader.getExtensionLoader(ProxyFactory.class).getExtension(proxyName);
		return proxyFactory.getProxy(interfaceClass, new RefererInvocationHandler<T>(interfaceClass, cluster));
	}

}
