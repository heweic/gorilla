package org.myframe.gorilla.config;

import java.util.concurrent.atomic.AtomicBoolean;

import org.myframe.gorilla.cluster.Cluster;
import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.config.handler.ConfigHandler;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.registry.AbstractRegistryFactory;
import org.myframe.gorilla.registry.Registry;
import org.myframe.gorilla.rpc.DefaultReferer;
import org.myframe.gorilla.rpc.Referer;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.transport.Client;
import org.myframe.gorilla.transport.EndpointFactory;
import org.myframe.gorilla.utils.ZkUtils;

/**
 * 引用配置
 */
public abstract class RefererConfig<T> extends AbstractConfig {

	public final static int DEFAULTPOOLSIZE = 3;

	/**
	 * 是否加载
	 */
	private AtomicBoolean initialized = new AtomicBoolean(false);
	/**
	 * 服务接口
	 */
	private Class<T> interfaceClass;

	/**
	 * 引用代理对象
	 */
	private volatile T ref;
	/**
	 * 
	 *  */
	private int poolSize = DEFAULTPOOLSIZE;

	private String diyNode;

	/**
	 * 初始化
	 */
	protected void initRef() throws Exception {

		if (initialized.get()) {
			return;
		}
		// 创建代理对象
		ConfigHandler configHandler = ExtensionLoader.getExtensionLoader(ConfigHandler.class)
				.getExtension(GorillaConstants.DEFAULT_VALUE);
		@SuppressWarnings("unchecked")
		Cluster<T> cluster = ExtensionLoader.getExtensionLoader(Cluster.class)
				.getExtension(GorillaConstants.DEFAULT_VALUE);

		ref = configHandler.refer(interfaceClass, cluster, GorillaConstants.DEFAULT_PROXY);
		//
		if (diyNode != null) {
			//
			String host = diyNode.split(":")[0];
			int port = Integer.parseInt(diyNode.split(":")[1]);
			URL url = new URL(host, port, interfaceClass.getName());
			
			EndpointFactory factory = ExtensionLoader.getExtensionLoader(EndpointFactory.class)
					.getExtension(GorillaConstants.DEFAULT_VALUE);
			Client client = factory.createClient(url);
			Referer<T> referer = new DefaultReferer<>(client, cluster.getInterface(), factory);
			cluster.addReferer(referer);
			cluster.init();
		}

		// 订阅服务
		Registry registry = new AbstractRegistryFactory().getRegistry();
		registry.subscribeService(interfaceClass, cluster);
		//
		initialized.set(true);
	}

	public T getRef() {
		if (null == ref) {
			try {
				synchronized (this) {
					if (null == ref) {
						initRef();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return ref;
	}

	public Class<T> getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(Class<T> interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public int getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(int poolSize) {
		this.poolSize = poolSize;
	}

	public String getDiyNode() {
		return diyNode;
	}

	public void setDiyNode(String diyNode) {
		this.diyNode = diyNode;
	}

}
