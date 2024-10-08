package org.myframe.gorilla.transport;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.myframe.gorilla.exception.BaseException;
import org.myframe.gorilla.rpc.Provider;
import org.myframe.gorilla.rpc.Request;

/**
 * 服务路由
 */
public abstract class AbstractRouter implements MessageHandler {

	/**
	 * 所有服务者
	 */
	private ConcurrentHashMap<String, Provider<?>> providers = new ConcurrentHashMap<String, Provider<?>>();

	/**
	 * 查找服务者
	 */
	public Provider<?> lookup(Request request) throws Exception {
		Provider<?> provider = providers.get(request.getInterfaceName());
		if (null == provider) {
			throw new BaseException("Provider does not exist." + request.getInterfaceName());
		}
		return provider;
	}

	/**
	 * 添加服务者
	 */
	public synchronized void addProviders(Provider<?> provider) throws Exception {
		this.providers.put(provider.getURL().getInterfaceName(), provider);
	}

	public Map<String, Provider<?>> getProviders() {
		return providers;
	}

}
