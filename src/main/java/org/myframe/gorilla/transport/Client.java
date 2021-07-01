package org.myframe.gorilla.transport;

import org.myframe.gorilla.rpc.Request;

public interface Client extends Endpoint {
	/**
	 * async send request.
	 *
	 * @param request
	 */
	void heartbeat(Request request);
}
