package org.myframe.gorilla.cluster;

import org.myframe.gorilla.rpc.Referer;

import java.util.List;

public interface Router<T> {

	public Referer<T> doWwitch(List<Referer<T>> referers);
}
