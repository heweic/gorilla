package org.myframe.gorilla.rpc;

public interface Response {

	/**
	 * 返回值
	 */
	public Object getValue();

	/**
	 * 异常
	 */
	public Exception getException();

	/**
	 * 调用ID
	 */
	public long getRequestId();

	/**
	 * 处理耗时
	 */
	public long getProcessTime();

	/**
	 * 设置耗时时间
	 */
	public void setProcessTime(long time);

	/**
	 * 是否成功
	 */
	public boolean isSuccess();
}
