package org.myframe.gorilla.test;

public class Info  {

	private String ipAddr;
	private int port;
	
	public Info(){}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	
	public static Info create(){
		Info info = new Info();
		
		info.setIpAddr("127.0.0.1");
		info.setPort(1234);
		return info;
	}

	@Override
	public String toString() {
		return ipAddr + "---" + port;
	}
	
	
	
	
	
}
