package org.myframe.gorilla.rpc;

/**
 * 节点接口
 * 
 * RPC客户端 服务端 都需要实现
 */
public interface Node {

	/**
	 * 初始化
	 */
	public void init();

	/**
	 * 销毁
	 */
	public void destroy();

	/**
	 * 是否可用
	 */
	public boolean isAvailable();

	/**
	 * 节点描述
	 */
	public String desc();

	/**
	 * 节点网络描述地址
	 */
	public URL getUrl();
}
