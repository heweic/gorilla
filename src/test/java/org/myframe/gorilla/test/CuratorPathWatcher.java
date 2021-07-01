package org.myframe.gorilla.test;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;

public class CuratorPathWatcher {

	public static void main(String[] args) throws Exception{
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1", retryPolicy);
		client.start();
		//
		
		PathChildrenCache watcher = new PathChildrenCache(client, "/zktest", true);
		
		watcher.getListenable().addListener(new PathChildrenCacheListener() {
			
			@Override
			public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
				
				PathChildrenCacheEvent.Type eventType = event.getType();
				 switch (eventType) {
                 case CONNECTION_RECONNECTED:
                	 watcher.rebuild();
                     break;
                 case CONNECTION_SUSPENDED:
                 case CONNECTION_LOST:
                     System.out.println("Connection error,waiting...");
                     break;
                 default:
                     System.out.println("PathChildrenCache changed : {path:" + event.getData().getPath() + " data:" +
                              "}");
                     print(watcher.getCurrentData());
             }
				
			}
		});
		
		watcher.start(StartMode.NORMAL);
		
		Thread.sleep(Long.MAX_VALUE);
		
		watcher.close();
	
	}
	private static void print(Object result) {
		System.out.println(result instanceof byte[] ? new String((byte[]) result) : result);
	}
}
