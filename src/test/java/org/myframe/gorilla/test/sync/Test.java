package org.myframe.gorilla.test.sync;

import org.myframe.gorilla.rpc.DefaultResponse;

import java.util.concurrent.ExecutionException;

public class Test {

	public static void main(String[] args) {
		TestFuture future = new TestFuture();
		DefaultResponse defaultResponse = new DefaultResponse();
		defaultResponse.setValue("value result 123456");
		future.setResponse(defaultResponse);
		
		Thread a = new Thread(new Runnable() {

			@Override
			public void run() {
				try {

					
					Object value = future.get();

					System.out.println("ThreadA" + "getvalue:--->" + value);
				} catch (InterruptedException | ExecutionException e) {
					e.printStackTrace();
				}

			}
		});
		Thread b = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {

					Thread.sleep(1000 *5);
					

					future.release();
				} catch (InterruptedException  e) {
					e.printStackTrace();
				}

			}
		});
		a.start();
		b.start();
		
	
	}

}
