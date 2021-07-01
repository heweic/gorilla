package org.myframe.gorilla.transport;

import org.myframe.gorilla.exception.BaseException;
import org.myframe.gorilla.rpc.Provider;
import org.myframe.gorilla.rpc.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务路由
 */
public abstract class AbstractRouter implements MessageHandler {

	/**
	 * 所有服务者
	 */
	private Map<String, Provider<?>> providers = new HashMap<String, Provider<?>>();

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
