package org.myframe.gorilla.rpc;

import org.myframe.gorilla.utils.ReflectUtil;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * 服务抽象类
 *  */
public abstract class AbstractProvider<T> implements Provider<T> {
	
	/**
	 * 服务提供者class
	 *  */
	protected Class<T> clz;
	/**
	 * 具体提供服务方法
	 *  */
	protected Map<String, Method> methodMap = new HashMap<String, Method>();
	/**
	 * 地址
	 *  */
	protected URL url;

	/** */
	public AbstractProvider(Class<T> clz, URL url) {
		this.clz = clz;
		initMethodMap(clz);
		//
		try {
			this.url = url;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public URL getURL() {
		return this.url;
	}

	@Override
	public Class<T> getInterface() {
		return clz;
	}

	/** 
	 * 根据请求找到对应的请求方法
	 * */
	protected Method lookup(Request request) throws Exception {
		String methodDesc = ReflectUtil.getMethodDesc(request.getMethodName(), request.getParamtersDesc());

		Method method = methodMap.get(methodDesc);

		return method;
	}

	private void initMethodMap(Class<?> clz) {
		Method[] methods = clz.getMethods();

		for (Method method : methods) {
			String methodDesc = ReflectUtil.getMethodDesc(method);
			methodMap.put(methodDesc, method);
		}
	}

	@Override
	public Response call(Request request) throws Exception {

		return this.invoke(request);
	}

	protected abstract Response invoke(Request request) throws Exception;

}
