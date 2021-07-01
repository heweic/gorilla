package org.myframe.gorilla.rpc;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.myframe.gorilla.common.ResponseStatFactory;

public class DefaultProvider<T> extends AbstractProvider<T> {

	protected Object proxyImpl;

	public DefaultProvider(Class<T> class1, Object proxyImpl, URL url) {
		super(class1, url);
		this.proxyImpl = proxyImpl;

	}

	@Override
	protected Response invoke(Request request) throws Exception {
		long start = System.currentTimeMillis();
		// 调用
		Method method = lookup(request);

		Object value = null;

		if (method.getReturnType().equals(Void.class)) {
			// @TODO 方法返回值为空的时候.. 可考虑直接返回，异步执行
		}
		try {
			value = method.invoke(proxyImpl, request.getArguments());
		} catch (InvocationTargetException e) {
			return faildResponse(e, request.getRequestId(), request.getInterfaceName(), method);
		} catch (Exception e) {
			return faildResponse(e, request.getRequestId(), request.getInterfaceName(), method);
		}

		if (null == value) {
			value = ResponseStatFactory.valueVOID();
		}

		// 成功调用 封装返回
		// DefaultResponse response = new DefaultResponse();
		// response.setValue(value);
		// response.setRequestId(request.getRequestId());
		// response.setException(ResponseStatFactory.createSuReturn());
		// response.setProcessTime(System.currentTimeMillis() - start);
		// //
		// return response;
		return successResponse(start, value, request, method);

	}

	private Response successResponse(long start, Object value, Request request, Method method) {
		long timeCost = System.currentTimeMillis() - start;
		DefaultResponse response = new DefaultResponse();
		response.setValue(value);
		response.setRequestId(request.getRequestId());
		response.setException(null);
		response.setProcessTime(timeCost);
		//
		return response;
	}

	private Response faildResponse(Exception exception, long requestId, String interfaceName, Method method) {
		DefaultResponse response = new DefaultResponse();
		response.setValue(ResponseStatFactory.valueVOID());
		response.setRequestId(requestId);
		response.setException(exception);
		response.setProcessTime(-1L);
		//
		return response;
	}

//	private static String FORMAT = "[java.lang.reflect.InvocationTargetException] tartgetException: [%s] message: [%s]";
//	private static String FORMAT_EXCEPTION = "[%s] message: [%s]";
//	private static String NULL = "null";

//	private String formatInvocationTargetException(InvocationTargetException e) {
//		//
//		String targetClassName;
//		String targetExMes;
//		//
//		Throwable target = e.getTargetException();
//
//		targetClassName = target.getClass().getName();
//		targetExMes = target.getMessage();
//
//		if (targetExMes == null) {
//			targetExMes = NULL;
//		}
//		//
//		return String.format(FORMAT, targetClassName, targetExMes);
//	}
//
//	private String formatException(Exception e) {
//		//
//		String exClassName;
//		String targetExMes;
//		//
//		exClassName = e.getClass().getName();
//		targetExMes = e.getMessage();
//
//		if (targetExMes == null) {
//			targetExMes = NULL;
//		}
//		//
//		return String.format(FORMAT_EXCEPTION, exClassName, targetExMes);
//	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String desc() {
		return null;
	}

	@Override
	public URL getUrl() {
		return url;
	}

}
