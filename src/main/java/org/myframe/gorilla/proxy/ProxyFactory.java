package org.myframe.gorilla.proxy;

import org.myframe.gorilla.extension.Spi;

import java.lang.reflect.InvocationHandler;

/**
 * 代理工厂
 *  */
@Spi
public interface ProxyFactory {

	/**
	 * 创建代理对象
	 *  */
	public <T> T getProxy(Class<T> interfaceClass, InvocationHandler invocationHandler);

}
