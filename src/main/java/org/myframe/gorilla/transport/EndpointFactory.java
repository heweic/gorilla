package org.myframe.gorilla.transport;

import org.myframe.gorilla.extension.Scope;
import org.myframe.gorilla.extension.Spi;
import org.myframe.gorilla.rpc.URL;

@Spi(scope=Scope.SINGLETON)
public interface EndpointFactory {
	/**
	 * create remote server
	 * 
	 * @param url
	 * @param messageHandler
	 * @return
	 */
	Server createServer(URL url, MessageHandler messageHandler) throws Exception;

	/**
	 * create remote client
	 * 
	 * @param url
	 * @return
	 */
	Client createClient(URL url) throws Exception;

	/**
	 * safe release server
	 * 
	 * @param server
	 * @param url
	 */
	void safeReleaseResource(Server server, URL url) throws Exception;

	/**
	 * safe release client
	 * 
	 * @param client
	 * @param url
	 */
	void safeReleaseResource(Client client, URL url) throws Exception;
}
