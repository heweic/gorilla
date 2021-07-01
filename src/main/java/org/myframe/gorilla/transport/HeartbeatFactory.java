package org.myframe.gorilla.transport;

import org.myframe.gorilla.extension.Scope;
import org.myframe.gorilla.extension.Spi;
import org.myframe.gorilla.rpc.Request;

@Spi(scope = Scope.SINGLETON)
public interface HeartbeatFactory {

	/**
	 * 创建心跳包
	 * 
	 * @return
	 */
	Request createRequest();

	/**
	 * 包装 handler，支持心跳包的处理
	 * 
	 * @param handler
	 * @return
	 */
	MessageHandler wrapMessageHandler(MessageHandler handler);
}
