package org.myframe.gorilla.test.sync;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Sync extends AbstractQueuedSynchronizer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 634468634143888315L;

	private final int done = 1; // 响应完成
	private final int pending = 0; // 远程服务器未响应

	@Override
	protected boolean tryAcquire(int arg) {

		return getState() == done ? true : false;
	}

	@Override
	protected boolean tryRelease(int arg) {
		if (getState() == pending) {
			if (compareAndSetState(pending, done)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDone() {
		getState();
		return getState() == done;
	}
}
