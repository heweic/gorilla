package org.myframe.gorilla.test;

import org.apache.zookeeper.*;
import org.apache.zookeeper.ZooDefs.Ids;

public class ZkTest {

	public static void main(String[] args) throws KeeperException, InterruptedException {
		try {
			ZooKeeper keeper = new ZooKeeper("127.0.0.1", 3000, new Watcher() {

				@Override
				public void process(WatchedEvent event) {
					
					if (event.getState() == Event.KeeperState.SyncConnected) {
						System.out.println("keeper 客服端连接创建完毕..");

					}
				}
			});
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			regNode(keeper, "/mytest", "" , CreateMode.PERSISTENT);
			regNode(keeper, "/mytest/com.test.service", "" , CreateMode.PERSISTENT);
			regNode(keeper, "/mytest/com.test.service/ip.port", "" , CreateMode.EPHEMERAL);
			
			Thread.sleep(Long.MAX_VALUE);
			
		} catch ( Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public static void regNode(ZooKeeper zooKeeper ,String addr , String value , CreateMode createMode) throws Exception{
		System.out.println("注册路径:" + addr);
		if (zooKeeper.exists(addr, false) == null) {
			zooKeeper.create(addr, value.getBytes(), Ids.OPEN_ACL_UNSAFE, createMode);
			System.out.println("注册路径:" + addr);
		}
		
		
	}

}
