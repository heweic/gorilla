package org.myframe.gorilla.rpc;

/**
 * RPC调用接口
 */
public interface Caller<T> extends Node {

	/**
	 * 客服端发起RPC调用 ,服务 端处理RPC调用
	 */
	public Response call(Request request) throws Exception;

	public Class<T> getInterface() throws Exception;
}
