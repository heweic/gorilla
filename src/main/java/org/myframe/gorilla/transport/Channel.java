package org.myframe.gorilla.transport;

import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.rpc.URL;

import java.net.InetSocketAddress;

public interface Channel {
	/**
	 * get local socket address.
	 * 
	 * @return local address.
	 */
	InetSocketAddress getLocalAddress();

	/**
	 * get remote socket address
	 * 
	 * @return
	 */
	InetSocketAddress getRemoteAddress();

	/**
	 * send request.
	 *
	 * @param request
	 * @return response future
	 * @throws TransportException
	 */
	Response request(Request request);

	/**
	 * open the channel
	 * 
	 * @return
	 */
	boolean open();

	/**
	 * close the channel.
	 */
	void close();

	/**
	 * close the channel gracefully.
	 */
	void close(int timeout);

	/**
	 * is closed.
	 * 
	 * @return closed
	 */
	boolean isClosed();

	/**
	 * the node available status
	 * 
	 * @return
	 */
	boolean isAvailable();
	/**
	 *  */
	URL getUrl();

}
