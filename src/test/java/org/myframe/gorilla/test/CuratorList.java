package org.myframe.gorilla.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorList {

	public static void main(String[] args) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1", retryPolicy);
		client.start();
		try {
			print(client.getChildren().forPath("/gorilla/org.myframe.gorilla.test.TestService"));
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	private static void print(Object result) {
		System.out.println(result instanceof byte[] ? new String((byte[]) result) : result);
	}
}
