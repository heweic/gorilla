package org.myframe.gorilla.transport;

import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.myframe.gorilla.rpc.URL;

public abstract class AbstractPoolClient extends AbstractClient {

	protected PooledObjectFactory<Channel> pooledObjectFactory;
	protected GenericObjectPool<Channel> genericObjectPool;
	protected GenericObjectPoolConfig config;
	private int maxTotal = 24;
	private int maxIdle  = 8;

	public AbstractPoolClient(URL url) {
		super(url);
	}

	public void initPool() {

		config = new GenericObjectPoolConfig();

		config.setMaxTotal(maxTotal);
		config.setMinIdle(maxIdle);
		config.setMaxIdle(maxIdle);

		pooledObjectFactory = createChannelFactory();
		genericObjectPool = new GenericObjectPool<>(pooledObjectFactory, config);

	}

	protected abstract PooledObjectFactory<Channel> createChannelFactory();
}
