package org.myframe.gorilla.transport;

/**
 * 服务接口
 */
public interface Server extends Channel {

	/**
	 * 初始化
	 */
	public void init() throws Exception;

}
