package org.myframe.gorilla.springsupport;

import org.myframe.gorilla.config.RefererConfig;
import org.myframe.gorilla.utils.ConcurrentHashSet;
import org.springframework.beans.factory.FactoryBean;

import java.util.Set;

public class RefererConfigBean<T> extends RefererConfig<T> implements FactoryBean<T> {

	/**
	 * 引用对象ID集合
	 *  */
	public static Set<String> ids = new ConcurrentHashSet<>();


	public RefererConfigBean() {
	}

	/**
	 * 获得object
	 *  */
	@Override
	public T getObject() throws Exception {
		return getRef();
	}

	/**
	 * 获得 object class
	 *  */
	@Override
	public Class<?> getObjectType() {
		return getInterfaceClass();
	}

	/**
	 * 是否是单例
	 *  */
	@Override
	public boolean isSingleton() {
		return true;
	}

}
