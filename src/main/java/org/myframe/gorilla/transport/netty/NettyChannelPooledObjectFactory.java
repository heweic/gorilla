package org.myframe.gorilla.transport.netty;

import org.apache.commons.pool2.PooledObject;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.DefaultPooledObject;
import org.myframe.gorilla.transport.Channel;

public class NettyChannelPooledObjectFactory implements PooledObjectFactory<Channel> {

	private NettyClient nettyClient;

	public NettyChannelPooledObjectFactory(NettyClient nettyClient) {
		this.nettyClient = nettyClient;
	}

	@Override
	public PooledObject<Channel> makeObject() throws Exception {
		NettyChannel nettyChannel = new NettyChannel(nettyClient);
		return new DefaultPooledObject<Channel>(nettyChannel);
	}

	@Override
	public void destroyObject(PooledObject<Channel> p) throws Exception {
		if (p.getObject() instanceof NettyChannel) {
			p.getObject().close();
		}
	}

	@Override
	public boolean validateObject(PooledObject<Channel> p) {
		if (p.getObject() instanceof NettyChannel) {
			return p.getObject().isAvailable();
		}
		return false;
	}

	@Override
	public void activateObject(PooledObject<Channel> p) throws Exception {
		if (p.getObject() instanceof NettyChannel) {
			final NettyChannel nettyChannel = (NettyChannel) p.getObject();
			if (!nettyChannel.isAvailable()) {
				nettyChannel.open();
			}
		}
	}

	@Override
	public void passivateObject(PooledObject<Channel> p) throws Exception {
		// TODO Auto-generated method stub

	}

}
