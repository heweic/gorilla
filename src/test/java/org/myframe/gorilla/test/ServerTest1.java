package org.myframe.gorilla.test;

import org.myframe.gorilla.utils.LoggerUtil;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ServerTest1 {
	public static void main(String[] args) {
		//
		new ClassPathXmlApplicationContext(new String[] { "classpath*:server.xml" });
		LoggerUtil.info("hello info message..");
		//
		//
		// NettyServer nettyServer = (NettyServer) NettyServer.getInstans();
		//

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
