package org.myframe.gorilla.rpc;

import org.myframe.gorilla.utils.ZkUtils;

public class URL {

	private String host;
	private int port;
	private String interfaceName;

	public URL(String host, int port, String interfaceName) {
		this.host = host;
		this.port = port;
		this.interfaceName = interfaceName;
	}

	public String getSuAddr() {
		return ZkUtils.toPath(interfaceName);
	}

	/**
	 * ip:port
	 */
	public String getValue() {
		return ZkUtils.toValue(host, port);
	}

	/**
	 * 
	 * node full path
	 * 
	 */
	public String getAddr() {

		return ZkUtils.toPath(interfaceName, getValue());

	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

}
