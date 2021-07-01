package org.myframe.gorilla.config;

import java.net.InetAddress;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.registry.AbstractRegistryFactory;
import org.myframe.gorilla.rpc.DefaultProvider;
import org.myframe.gorilla.rpc.Provider;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.springsupport.ServiceConfigBean;
import org.myframe.gorilla.transport.EndpointFactory;
import org.myframe.gorilla.transport.ProviderMessageRouter;
import org.myframe.gorilla.transport.Server;
import org.myframe.gorilla.utils.ConcurrentHashSet;
import org.myframe.gorilla.utils.LoggerUtil;
import org.myframe.gorilla.utils.ReflectUtil;
import org.springframework.beans.factory.BeanFactory;

/**
 * 服务配置
 */
public abstract class ServiceConfig<T> extends AbstractConfig {

	public static ConcurrentHashSet<ServiceConfigBean<?>> service = new ConcurrentHashSet<>();
	private volatile static ProviderMessageRouter mockProviderMessageRouter = null;
	public static HashSet<Provider<?>> providers = new HashSet<>();

	/**
	 * 接口class
	 */
	private String interfaceClass;
	/**
	 * 接口实现对象
	 */
	private String ref;
	/**
	 * 对象工厂
	 */
	protected BeanFactory beanFactory;
	/**
	 * 是否加载
	 */
	private AtomicBoolean exported = new AtomicBoolean(false);
	/**
	 * 服务者
	 */
	private static Server server;

	private static final ReentrantLock lock = new ReentrantLock();

	public ServiceConfig() {
	}

	public ServiceConfig(String interfaceClass, String ref) {
		this.interfaceClass = interfaceClass;
		this.ref = ref;
	}

	/**
	 * 加载
	 */
	public synchronized void export() {
		if (exported.get()) {
			return;
		}
		//
		LoggerUtil.info("do export service ...");
		doExport();
	}

	public static void addProvider(Provider<?> provider) {
		providers.add(provider);
	}

	private void doExport() {
		try {
			//
			LoggerUtil.info("set target ref bean. " + ref);
			lock.lock();

			if (null == server) {

				URL url = new URL(InetAddress.getLocalHost().getHostAddress(), AbstractConfig.providerPort,
						interfaceClass);
				Object obj = beanFactory.getBean(ref);
				Provider<?> provider = new DefaultProvider<>(ReflectUtil.forName(interfaceClass), obj, url);
				addProvider(provider);
				//
				EndpointFactory endpointFactory = ExtensionLoader.getExtensionLoader(EndpointFactory.class)
						.getExtension(GorillaConstants.DEFAULT_VALUE);

				server = endpointFactory.createServer(url, initProviderMessageRouter(provider));
				server.init();

			} else {
				URL url = new URL(InetAddress.getLocalHost().getHostAddress(), AbstractConfig.providerPort,
						interfaceClass);
				
				Object obj = beanFactory.getBean(ref);
				
				Provider<?> provider = new DefaultProvider<>(ReflectUtil.forName(interfaceClass), obj, url);
				addProvider(provider);
				
				initProviderMessageRouter(provider);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			lock.unlock();
		}
	}

	public ProviderMessageRouter initProviderMessageRouter(Provider<?> provider) throws Exception {
		if (null == mockProviderMessageRouter) {
			synchronized (this) {
				if (null == mockProviderMessageRouter) {
					mockProviderMessageRouter = new ProviderMessageRouter();
				}
			}
		}
		//
		mockProviderMessageRouter.addProviders(provider);
		// 注册服务
		new AbstractRegistryFactory().getRegistry().publishService(provider);
		//
		return mockProviderMessageRouter;
	}

	public AtomicBoolean getExported() {
		return exported;
	}

	public String getInterfaceClass() {
		return interfaceClass;
	}

	public void setInterfaceClass(String interfaceClass) {
		this.interfaceClass = interfaceClass;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

}
