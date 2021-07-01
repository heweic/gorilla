package org.myframe.gorilla.transport;

import org.myframe.gorilla.rpc.Provider;
import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.utils.LoggerUtil;

public class ProviderMessageRouter extends AbstractRouter {

	@Override
	public Object handle(Channel channel, Object message) {
		try {
			Request request = (Request) message;

			Provider<?> provider = lookup(request);

			return provider.call(request);
		} catch (Exception e) {
			LoggerUtil.error(e.getMessage());
			return null;
		}
	}

}
