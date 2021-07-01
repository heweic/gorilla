package org.myframe.gorilla.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorTest {

	private static final String ZK_PATH = "/zktest";

	public static void main(String[] args) throws Exception {
		//

		//
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1", retryPolicy);
		client.start();

		// 2.Client API test
		// 2.1 Create node
//		if(client.checkExists().forPath(ZK_PATH) != null){
//			client.delete().forPath(ZK_PATH);
//			System.out.println("删除根节点..");
//		}
		
		
//		String data1 = "hello";
//		print("create", ZK_PATH, data1);
//		client.create().creatingParentsIfNeeded().forPath(ZK_PATH, data1.getBytes());
		
		//client.create().creatingParentsIfNeeded().forPath(ZK_PATH + "/app3" , "127.0.0.1".getBytes());
		client.create().creatingParentsIfNeeded().forPath(ZK_PATH + "/app4" , "127.0.0.1".getBytes());
		
		print(client.getChildren().forPath("/"));
//		

//		// 2.4 Remove node
//		print("delete", ZK_PATH);
//		client.delete().forPath(ZK_PATH);
//		print("ls", "/");
//		print(client.getChildren().forPath("/"));
	}

	private static void print(String... cmds) {
		StringBuilder text = new StringBuilder("$ ");
		for (String cmd : cmds) {
			text.append(cmd).append(" ");
		}
		System.out.println(text.toString());
	}

	private static void print(Object result) {
		System.out.println(result instanceof byte[] ? new String((byte[]) result) : result);
	}
}
