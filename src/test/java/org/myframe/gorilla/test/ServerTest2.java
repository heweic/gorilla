package org.myframe.gorilla.test;

import org.myframe.gorilla.utils.LoggerUtil;

public class ServerTest2 {
	public static void main(String[] args) {
		//
	//	ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "classpath*:server2.xml" });
		LoggerUtil.info("hello info message..");
		//
		//
		// NettyServer nettyServer = (NettyServer) NettyServer.getInstans();
		//

		try {
			Thread.sleep(Long.MAX_VALUE);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
