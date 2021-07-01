package org.myframe.gorilla.rpc;

/**
 * 服务者接口
 *  */
public interface Provider<T> extends Caller<T> {

	public URL getURL();

}
