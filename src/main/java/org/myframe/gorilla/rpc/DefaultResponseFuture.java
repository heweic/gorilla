package org.myframe.gorilla.rpc;

import org.myframe.gorilla.common.FutureState;
import org.myframe.gorilla.exception.CallTimeOutException;

public class DefaultResponseFuture implements ResponseFuture, Response {

	// private volatile CountDownLatch countDownLatch = new CountDownLatch(1);

	private final Object lock = new Object();

	private FutureState state = FutureState.DOING;

	//
	private Request request;
	//
	private Object result;
	private Exception exception = null;

	private long createTime = System.currentTimeMillis();;
	private long timeOut;
	private long processTime;

	public DefaultResponseFuture(Request request, long timeOut) {
		this.request = request;
		this.timeOut = timeOut;

	}

	private boolean isDoing() {
		return state.isDoingState();
	}

	@Override
	public boolean cancel() {
		synchronized (lock) {
			if (!isDoing()) {
				return false;
			}
			//
			state = FutureState.DOING;

			return true;
		}
	}

	private void timeoutSoCancel() {
		this.processTime = System.currentTimeMillis() - createTime;

		synchronized (lock) {
			if (!isDoing()) {
				return;
			}

			state = FutureState.CANCELLED;
			exception = new CallTimeOutException();

			lock.notifyAll();
		}

	}

	protected boolean cancel(Exception e) {
		synchronized (lock) {
			if (!isDoing()) {
				return false;
			}

			state = FutureState.CANCELLED;
			exception = e;
			lock.notifyAll();
		}

		return true;
	}

	@Override
	public boolean isCancelled() {
		return state.isCancelledState();
	}

	@Override
	public boolean isDone() {
		return state.isDoneState();
	}

	@Override
	public boolean isSuccess() {
		return state.isDoneState() && exception == null;
	}

	@Override
	public Object getValue() {
		synchronized (lock) {
			if (!isDoing()) {
				return getValueOrThrowable();
			}

			if (timeOut <= 0) {
				try {
					lock.wait();
				} catch (Exception e) {
					cancel();
				}
			} else {
				long waitTime = timeOut - (System.currentTimeMillis() - createTime);

				if (waitTime > 0) {
					for (;;) {
						try {
							lock.wait(waitTime);
						} catch (InterruptedException e) {
						}

						if (!isDoing()) {
							break;
						} else {
							waitTime = timeOut - (System.currentTimeMillis() - createTime);
							if (waitTime <= 0) {
								break;
							}
						}
					}
				}

				if (isDoing()) {
					timeoutSoCancel();
				}
			}
			return getValueOrThrowable();
		}
	}

	private Object getValueOrThrowable() {

		return result;
	}

	@Override
	public Exception getException() {
		return exception;
	}

	@Override
	public long getRequestId() {
		return request.getRequestId();
	}

	@Override
	public long getProcessTime() {
		return processTime;
	}

	@Override
	public void setProcessTime(long time) {
		processTime = time;
	}

	@Override
	public void onSuccess(Response response) {
		this.result = response.getValue();
		this.processTime = response.getProcessTime();

		done();
	}

	@Override
	public void onFailure(Response response) {
		this.exception = response.getException();
		done();
	}

	protected boolean done() {
		synchronized (lock) {
			if (!isDoing()) {
				return false;
			}

			state = FutureState.DONE;
			lock.notifyAll();
		}

		return true;
	}

	@Override
	public long getCreateTime() {
		return createTime;
	}

	@Override
	public long getTimeOut() {
		return timeOut;
	}

}
