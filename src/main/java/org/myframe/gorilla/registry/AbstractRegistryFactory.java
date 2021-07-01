package org.myframe.gorilla.registry;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.myframe.gorilla.config.AbstractConfig;
import org.myframe.gorilla.springsupport.RegistryConfigBean;

public class AbstractRegistryFactory implements RegistryFactory {

	private volatile static Registry registry;

	@Override
	public Registry getRegistry() throws Exception {

		if (null == registry) {
			synchronized (AbstractRegistryFactory.class) {
				if (null == registry) {

					RegistryConfigBean registryConfigBean = AbstractConfig.configBean;

					RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
					CuratorFramework client = CuratorFrameworkFactory.newClient(registryConfigBean.toRegistryAddress(),
							retryPolicy);
					registry = new ZookeeperRegistry(client);
					client.getConnectionStateListenable().addListener(new ReTryConnectionStateListener(registry));
					
					client.start();	
				}
			}
		}
		return registry;
	}

}
