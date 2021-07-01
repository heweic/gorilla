package org.myframe.gorilla.transport;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.exception.BaseException;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.transport.netty.NettyClient;
import org.myframe.gorilla.transport.netty.NettyServer;
import org.myframe.gorilla.utils.LoggerUtil;

public abstract class AbstractEndpointFactory implements EndpointFactory {

	protected static Map<String, Server> ipPort2ServerShareChannel = new HashMap<String, Server>();

	protected volatile static ConcurrentMap<Client, HeartbeatFactory> endpoints = new ConcurrentHashMap<Client, HeartbeatFactory>();

	protected volatile static ConcurrentMap<String, Client> clients = new ConcurrentHashMap<String, Client>();

	private static final Object lock = new Object();

	public AbstractEndpointFactory() {

	}

	@Override
	public Server createServer(URL url, MessageHandler messageHandler) throws Exception {
		String ipPort = String.valueOf(url.getPort());
		synchronized (ipPort2ServerShareChannel) {
			Server server = this.ipPort2ServerShareChannel.get(ipPort);
			if (null != server) {
				return server;
			}
			// new sever
			HeartbeatFactory factory = ExtensionLoader.getExtensionLoader(HeartbeatFactory.class)
					.getExtension(GorillaConstants.DEFAULT_VALUE);
			MessageHandler handler = factory.wrapMessageHandler(messageHandler);
			//
			server = new NettyServer(handler);
			this.ipPort2ServerShareChannel.put(ipPort, server);
			return server;
		}

	}

	@Override
	public Client createClient(URL url) throws Exception {

		if (clients.containsKey(url.getValue())) {
			LoggerUtil.error("has URL : " + url);
			return clients.get(url.getValue());
		}

		synchronized (lock) {
			//
			if (clients.containsKey(url.getValue())) {
				LoggerUtil.error("has URL : " + url);
				return clients.get(url.getValue());
			}

			LoggerUtil.info(this.getClass().getSimpleName() + " create client: url={}", url);

			//
			Client client = new NettyClient(url);
			client.open();

			HeartbeatFactory heartbeatFactory = ExtensionLoader.getExtensionLoader(HeartbeatFactory.class)
					.getExtension(GorillaConstants.DEFAULT_VALUE);

			if (heartbeatFactory == null) {
				throw new BaseException("HeartbeatFactory not exist: " + GorillaConstants.DEFAULT_VALUE);
			}

			endpoints.put(client, heartbeatFactory);
			clients.put(url.getValue(), client);
			//
			return client;
		}
	}

	@Override
	public void safeReleaseResource(Server server, URL url) throws Exception {

	}

	@Override
	public void safeReleaseResource(Client client, URL url) throws Exception {
		// close client
		client.close();
		// delete endpoints
		endpoints.remove(client);
	}

}
