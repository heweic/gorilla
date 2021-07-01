
package org.myframe.gorilla.proxy.spi;

import org.myframe.gorilla.extension.SpiMeta;
import org.myframe.gorilla.proxy.ProxyFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/** 
 * JDK 接口代理
 * */
@SpiMeta(name = "jdk")
public class JdkProxyFactory implements ProxyFactory {

	@SuppressWarnings("unchecked")
	public <T> T getProxy(Class<T> clz, InvocationHandler invocationHandler) {
		return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] { clz }, invocationHandler);
	}

}
