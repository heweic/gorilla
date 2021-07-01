package org.myframe.gorilla.cluster;

import org.myframe.gorilla.extension.Scope;
import org.myframe.gorilla.extension.Spi;

@Spi(scope = Scope.SINGLETON)
public interface ClusterSupport<T> {

	public void child_added(byte[] data, Cluster<T> cluster , Class<T> clazz) throws Exception;

	public void child_updated(byte[] data, Cluster<T> cluster , Class<T> clazz) throws Exception;

	public void child_removed(byte[] data, Cluster<T> cluster , Class<T> clazz) throws Exception;

	
}
