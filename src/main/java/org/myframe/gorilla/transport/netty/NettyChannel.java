package org.myframe.gorilla.transport.netty;

import io.netty.channel.ChannelFuture;
import org.myframe.gorilla.common.ChannelState;
import org.myframe.gorilla.rpc.DefaultResponseFuture;
import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.transport.Channel;
import org.myframe.gorilla.utils.LoggerUtil;

import java.net.InetSocketAddress;

public class NettyChannel implements Channel {

	private NettyClient nettyClient;
	private volatile ChannelState state = ChannelState.UNINIT;
	private io.netty.channel.Channel channel;

	private InetSocketAddress remoteAddress = null;
	private InetSocketAddress localAddress = null;

	public NettyChannel(NettyClient nettyClient) {
		this.nettyClient = nettyClient;
		this.remoteAddress = new InetSocketAddress(nettyClient.getUrl().getHost(), nettyClient.getUrl().getPort());
		this.localAddress = nettyClient.getLocalAddress();
	}

	@Override
	public InetSocketAddress getLocalAddress() {
		return localAddress;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		return remoteAddress;
	}

	@Override
	public Response request(Request request) {
		try {

			DefaultResponseFuture future = new DefaultResponseFuture(request, 5000);
			nettyClient.registerCallback(request.getRequestId(), future);
			channel.writeAndFlush(request);

			//
			return future;
		} catch (Exception e) {
			e.printStackTrace();
			LoggerUtil.error(" nettyClient request  erro  ..." + e.getClass() + e.getMessage());

			return null;
		}
	}

	@Override
	public boolean open() {
		if (isAvailable()) {
			LoggerUtil.warn("the channel already open, local: " + localAddress + " remote: " + remoteAddress + " url: "
					+ nettyClient.getUrl().getHost());
			return true;
		}
		try {
			ChannelFuture channelFuture = nettyClient.getBootstrap()
					.connect(nettyClient.getUrl().getHost(), nettyClient.getUrl().getPort()).sync();

			if (channelFuture.isSuccess()) {
				channel = channelFuture.channel();
				state = ChannelState.ALIVE;
				//
			}
			return true;
		} catch (Exception e) {
			LoggerUtil.error("open nettyChannel failed :" + nettyClient.getUrl().getHost() + ":"
					+ nettyClient.getUrl().getPort());
		}
		return false;
	}

	@Override
	public void close() {
		close(1);
	}

	@Override
	public void close(int timeout) {
		try {
			state = ChannelState.CLOSE;
			if (channel != null) {
				channel.close();
			}
		} catch (Exception e) {
			LoggerUtil.error("NettyChannel close Error: " + nettyClient.getUrl().getHost() + " local=" + localAddress,
					e);
		}

	}

	@Override
	public boolean isClosed() {
		return state.isCloseState();
	}

	@Override
	public boolean isAvailable() {
		return state.isAliveState() && channel.isActive();
	}

	@Override
	public URL getUrl() {
		return nettyClient.getUrl();
	}

}
