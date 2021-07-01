package org.myframe.gorilla.rpc;

import java.util.concurrent.locks.AbstractQueuedSynchronizer;

public class Sync extends AbstractQueuedSynchronizer {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// future status
	private final int done = 1;    // 响应完成
	private final int pending = 0; // 远程服务器未响应

	/**
	 * 是否可以获得锁
	 */
	protected boolean tryAcquire(int acquires) {
		// 如果响应完成，可获得锁
		return getState() == done ? true : false;
	}

	/**
	 * 
	 * 释放锁
	 */
	protected boolean tryRelease(int releases) {
		if (getState() == pending) {
			if (compareAndSetState(pending, done)) {
				return true;
			}
		}
		return false;
	}

	public boolean isDone() {
		return getState() == done;
	}

}
