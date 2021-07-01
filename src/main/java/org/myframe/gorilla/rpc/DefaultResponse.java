package org.myframe.gorilla.rpc;

public class DefaultResponse extends AbstractMessage implements Response {

	public static final String NONE = "NONE";

	private long requestId;
	private long processTime;
	private int timeout;

	private Object value;

	private Exception exception;

	public DefaultResponse() {

	}

	public DefaultResponse(Object value) {
		this.value = value;
	}

	@Override
	public Object getValue() {
		return value;
	}

	@Override
	public Exception getException() {
		return exception;
	}

	@Override
	public long getRequestId() {
		return requestId;
	}

	@Override
	public long getProcessTime() {
		return processTime;
	}

	@Override
	public void setProcessTime(long time) {
		this.processTime = time;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public void setException(Exception exception) {
		if (null != exception) {
			this.exception = exception;
		}
	}

	public void setExceptionNone() {
		this.exception = null;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	@Override
	public boolean isSuccess() {
		return null != value && exception == null;
	}

}
