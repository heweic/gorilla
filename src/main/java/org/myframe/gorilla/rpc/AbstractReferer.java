package org.myframe.gorilla.rpc;

import org.myframe.gorilla.transport.Client;

public abstract class AbstractReferer<T> implements Referer<T> {

	protected Client client;
	protected Class<T> clzz;

	public AbstractReferer(Client client, Class<T> clzz) {
		super();
		this.client = client;
		this.clzz = clzz;
	}

	@Override
	public Response call(Request request) throws Exception {
		return client.request(request);
	}

}
