package org.myframe.gorilla.test.sync;

import org.myframe.gorilla.rpc.Response;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class TestFuture implements Future<Object> {

	private Sync sync;

	private Response response;

	
	
	public Response getResponse() {
		return response;
	}

	public void release(){
		this.sync.release(1);
	}
	public void setResponse(Response response) {
		this.response = response;
		//sync.release(1);
	}

	public TestFuture() {
		sync = new Sync();
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCancelled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isDone() {
		return sync.isDone();
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		sync.acquire(-1);
		if (this.response != null) {
			return this.response.getValue();
		} else {
			return null;
		}
	}

	@Override
	public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
		boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
		if (success) {
			if (this.response != null) {
				return this.response.getValue();
			} else {
				return null;
			}
		} else {
			throw new RuntimeException("");
		}
	}

}
