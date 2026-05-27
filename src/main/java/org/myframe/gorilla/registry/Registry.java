package org.myframe.gorilla.registry;

import org.myframe.gorilla.cluster.Cluster;
import org.myframe.gorilla.rpc.Provider;

/**
 * 服务注册端口
 * 
 * @author hew
 *
 *         <pre>
 * 
 *         </pre>
 */
public interface Registry {

	/** 服务注册 */
	public void publishService(Provider<?> provider) throws Exception;

	/** 订阅服务 */
	public void subscribeService(Class<?> clz, Cluster<?> cluster) throws Exception;

}
