package org.myframe.gorilla.rpc;

public interface Request {

	/**
	 * 接口
	 */
	public String getInterfaceName();

	/**
	 * 方法名
	 */
	public String getMethodName();

	/**
	 * 参数描述
	 */
	public String getParamtersDesc();

	/**
	 * 参数
	 */
	public Object[] getArguments();

	/**
	 * 调用ID
	 */
	public long getRequestId();

	/**
	 * 请求信息
	 */
	public String info();
}
