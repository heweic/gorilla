package org.myframe.gorilla.registry;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.framework.state.ConnectionStateListener;
import org.myframe.gorilla.config.ServiceConfig;
import org.myframe.gorilla.rpc.Provider;
import org.myframe.gorilla.utils.LoggerUtil;

import java.util.Iterator;

public class ReTryConnectionStateListener implements ConnectionStateListener {

	private boolean hasLost = false;
	private Registry registry;
	

	public ReTryConnectionStateListener(Registry registry) {
		this.registry = registry;
	}



	@Override
	public void stateChanged(CuratorFramework client, ConnectionState newState) {
		if (newState == ConnectionState.LOST) {

			LoggerUtil.error("ZK 会话丢失....");
			hasLost = true;

		}
		if (newState == ConnectionState.RECONNECTED) {

			LoggerUtil.error("zk 重连成功....");

			if (hasLost) {

				LoggerUtil.error("会话丢失后重连成功......");

				Iterator<Provider<?>> iterator = ServiceConfig.providers.iterator();
				while(iterator.hasNext()){
					Provider<?> p = iterator.next();
					try {
						registry.publishService(p);
					} catch (Exception e) {
						e.printStackTrace();
						LoggerUtil.error("重新发布服务失败....");
					}
				}
				hasLost = false;
			}
		}

	}

}
