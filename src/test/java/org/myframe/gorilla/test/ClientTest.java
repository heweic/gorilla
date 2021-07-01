package org.myframe.gorilla.test;

import org.apache.log4j.LogManager;
import org.myframe.gorilla.test.inter.Inter1;
import org.myframe.gorilla.test.inter.Inter2;
import org.myframe.gorilla.test.inter.Inter3;
import org.myframe.gorilla.test.inter.Inter4;
import org.myframe.gorilla.test.inter.Inter5;
import org.myframe.gorilla.utils.LoggerUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ClientTest {

	private org.apache.log4j.Logger log = LogManager.getLogger(ClientTest.class);

	
	public void test() {
		ApplicationContext  context = new ClassPathXmlApplicationContext(new String[] { "classpath*:client.xml" });
		
		System.out.println(context.getBean(Inter1.class).test("111"));
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//
		ApplicationContext context;
		for(int i = 0 ; i < 1 ; i ++ ) {
			 context = new ClassPathXmlApplicationContext(new String[] { "classpath*:client.xml" });
			LoggerUtil.info("hello info message..");


			
			context.getBean(Inter1.class);
			context.getBean(Inter2.class);
			context.getBean(Inter3.class);
			context.getBean(Inter4.class);
			context.getBean(Inter5.class);
			
			try {
				Thread.sleep(1000 * 2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(context.getBean(Inter1.class).test("111"));
			System.out.println(context.getBean(Inter2.class).test("111"));
			System.out.println(context.getBean(Inter3.class).test("111"));
			System.out.println(context.getBean(Inter4.class).test("111"));
			System.out.println(context.getBean(Inter5.class).test("111"));

			
			System.out.println("==========================");
			
			
		}
		//
		//
		// NettyServer nettyServer = (NettyServer) NettyServer.getInstans();
		//

//		TestService testService = (TestService) context.getBean("testService");
//
//		try {
//			Thread.sleep(1000 * 5);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int  i = 0 ; i < 10000 ;  i ++ ) {
//			 RandomStringUtils.random(100, "ABCCCCDSKJKJKLLLKKLK:L").getBytes();
//			
//		}
//		
//		for (int i = 0; i < 10; i++) {
//			try {
//				String result = testService.sayHello("3");
//				System.out.println(result == null);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			;
//		}

//		Executor executor = Executors.newCachedThreadPool();
//		
//		List<String > result = new ArrayList<>();
//		
//		long startTime = System.currentTimeMillis();
//		int size = 10000 * 1;
//		
//		
//		Thread t = new Thread(new Runnable() {
//			
//			@Override
//			public void run() {
//				for(int  i = 0 ; i < size ;  i ++ ) {
//					byte[] bs =  RandomStringUtils.random(100, "ABCCCCDSKJKJKLLLKKLK:L").getBytes();
//					result.add(testService.say3(bs));
//				}
//			}
//		});
//		t.start();
//		
//		while(true) {
//			try {
//				Thread.sleep(1);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			
//			if(result.size() % 1000  == 0) {
//				System.out.println("整千");
//			}
//			
//			if(result.size() >= size) {
//				System.out.println("总耗时:" + (System.currentTimeMillis() - startTime));
//				break;
//			}
//		}

	}

}
