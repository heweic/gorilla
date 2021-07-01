
package org.myframe.gorilla.config.handler;

import org.myframe.gorilla.cluster.Cluster;
import org.myframe.gorilla.extension.Scope;
import org.myframe.gorilla.extension.Spi;

@Spi(scope = Scope.SINGLETON)
public interface ConfigHandler {

	public <T> T refer(Class<T> interfaceClass, Cluster<T> cluster, String proxyName);

}
