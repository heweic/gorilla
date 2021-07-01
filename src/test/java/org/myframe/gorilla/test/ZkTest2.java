package org.myframe.gorilla.test;

import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class ZkTest2 {

	public static void main(String[] args) {
		try {
			ZooKeeper keeper = new ZooKeeper("127.0.0.1", 3000, new Watcher() {

				@Override
				public void process(WatchedEvent event) {

					if (event.getState() == Event.KeeperState.SyncConnected) {
						System.out.println("keeper 客服端连接创建完毕..");

					}
					System.out.println(event.getState());
				}
			});
			
			keeper.exists("/mytest/com.test.service/ip.port", new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					System.out.println("watchedEvet:------>" + event.getState());
				}
			});
			
			keeper.register(new Watcher() {
				
				@Override
				public void process(WatchedEvent event) {
					System.out.println("register watcher:--->" + event.getType());
					
				}
			});
			try {
				Thread.sleep(Long.MAX_VALUE);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		} catch (Exception e) {

			System.out.println(e.getMessage());
		}
	}
}
