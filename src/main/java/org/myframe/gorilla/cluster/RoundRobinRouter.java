package org.myframe.gorilla.cluster;

import org.myframe.gorilla.rpc.Referer;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RoundRobinRouter<T> implements Router<T> {

	private AtomicInteger idx = new AtomicInteger(0);

	@Override
	public Referer<T> doWwitch(List<Referer<T>> referers) {
		int index = getNextPositive();

		for (int i = 0; i < referers.size(); i++) {
			Referer<T> ref = referers.get((i + index) % referers.size());
			if (ref.isAvailable()) {
				return ref;
			}
		}
		return null;
	}

	private int getNextPositive() {
		return 0x7fffffff & (idx.incrementAndGet());
	}

}
