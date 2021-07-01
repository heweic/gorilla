package org.myframe.gorilla.rpc;

/**
 * 对服务端引用接口
 *  */
public interface Referer<T> extends Caller<T>{

	public String interfaceName();
	
}
