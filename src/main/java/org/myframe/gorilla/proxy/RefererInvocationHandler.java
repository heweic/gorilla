package org.myframe.gorilla.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import org.myframe.gorilla.cluster.Cluster;
import org.myframe.gorilla.common.ResponseStatFactory;
import org.myframe.gorilla.exception.BaseException;
import org.myframe.gorilla.exception.GorillaServiceException;
import org.myframe.gorilla.rpc.DefaultRequest;
import org.myframe.gorilla.rpc.DefaultResponseFuture;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.utils.LoggerUtil;
import org.myframe.gorilla.utils.ReflectUtil;
import org.myframe.gorilla.utils.RequestIdGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RefererInvocationHandler<T> implements InvocationHandler {

	public static Logger log = LoggerFactory.getLogger(RefererInvocationHandler.class);

	private Class<T> clz;
	private Cluster<T> cluster;

	public RefererInvocationHandler(Class<T> clz, Cluster<T> cluster) {
		this.clz = clz;
		this.cluster = cluster;

	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		if (isLocalMethod(method)) {
			if ("toString".equals(method.getName())) {
				return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy))
						+ ", with InvocationHandler " + this;
			}
			throw new GorillaServiceException("can not invoke local method:" + method.getName());
		}

		DefaultRequest request = new DefaultRequest();

		request.setRequestId(RequestIdGenerator.getRequestId());
		request.setArguments(args);
		request.setMethodName(method.getName());
		request.setParamtersDesc(ReflectUtil.getMethodParamDesc(method));
		request.setInterfaceName(clz.getName());

		Response response = null;
		try {
			response = cluster.call(request);
		} catch (Exception e) {
			throw e;
		}
		if (null == response) {
			return null;
		}

		// response is future
		DefaultResponseFuture future = (DefaultResponseFuture) response;

		// Causes the current thread to wait until the latch has counted down to
		// zero
		// timeout 5s
		future.getValue();

		if (!future.isSuccess()) {

			Object exObj = null;

			//
			if (null == future.getValue()) {
				exObj = "gorilla response is null , request info :" + request.info();
			} else {
				exObj = future.getException();
			}

			//
			LoggerUtil.error("服务方逻辑抛出异常..调用ID:" + request.getRequestId() + "调用接口：" + request.getInterfaceName()
					+ "调用方法：" + request.getMethodName() + "参数描述:" + request.getParamtersDesc() + "异常信息:"
					+ exObj.toString());
			//
			throw new BaseException(exObj.toString());
		}
		//
		return ResponseStatFactory.isVOID(response.getValue()) ? null : response.getValue();
	}

	/**
	 * tostring,equals,hashCode,finalize等接口未声明的方法不进行远程调用
	 * 
	 * @param method
	 * @return
	 */
	public boolean isLocalMethod(Method method) {
		if (method.getDeclaringClass().equals(Object.class)) {
			try {
				clz.getDeclaredMethod(method.getName(), method.getParameterTypes());
				return false;
			} catch (Exception e) {
				return true;
			}
		}
		return false;
	}
}
