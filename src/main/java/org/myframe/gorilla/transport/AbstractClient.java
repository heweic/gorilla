package org.myframe.gorilla.transport;

import org.myframe.gorilla.codec.Codec;
import org.myframe.gorilla.common.ChannelState;
import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.transport.netty.DefaultRpcCodec;

import java.net.InetSocketAddress;

public abstract class AbstractClient implements Client {

	protected InetSocketAddress localAddress;
	protected InetSocketAddress remoteAddress;
	protected URL url;
	protected Codec codec;
	protected Serialization serialization;
	protected volatile ChannelState state = ChannelState.UNINIT;

	public AbstractClient(URL url) {
		this.url = url;
		this.serialization = ExtensionLoader.getExtensionLoader(Serialization.class)
				.getExtension(GorillaConstants.DEFAULT_VALUE);
		this.codec = new DefaultRpcCodec(serialization);

	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	public void setLocalAddress(InetSocketAddress localAddress) {
		this.localAddress = localAddress;
	}

	public void setRemoteAddress(InetSocketAddress remoteAddress) {
		this.remoteAddress = remoteAddress;
	}

	@Override
	public URL getUrl() {
		return this.url;
	}

}
