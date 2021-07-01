package org.myframe.gorilla.transport;

/**
 * 
 */
public interface MessageHandler {

	/**
	 * 处理请求
	 */
	public  Object handle(Channel channel, Object message);

}
