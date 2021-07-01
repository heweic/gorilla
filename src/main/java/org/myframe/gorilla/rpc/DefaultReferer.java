package org.myframe.gorilla.rpc;

import org.myframe.gorilla.transport.Client;
import org.myframe.gorilla.transport.EndpointFactory;
import org.myframe.gorilla.utils.LoggerUtil;

public class DefaultReferer<T> extends AbstractReferer<T> {

	
	private EndpointFactory endpointFactory;
	
	public DefaultReferer(Client client, Class<T> class1 , EndpointFactory endpointFactory) {
		super(client, class1);
		//
		this.endpointFactory = endpointFactory;
	}

	@Override
	public Class<T> getInterface() throws Exception {
		return clzz;
	}

	@Override
	public void init() {
		
	}

	@Override
	public void destroy() {
		try{
			endpointFactory.safeReleaseResource(client, getUrl());
		}catch(Exception e){
			LoggerUtil.error(" destory DefaultReferer erro .. " + e.getMessage());
		}
	}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public String desc() {
		
		return null;
	}

	@Override
	public URL getUrl() {
		return client.getUrl();
	}
	
	public String interfaceName() {
		return getUrl().getInterfaceName();
	}

}
