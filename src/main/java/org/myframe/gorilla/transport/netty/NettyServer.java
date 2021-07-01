package org.myframe.gorilla.transport.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.springsupport.ServicePortConfigBean;
import org.myframe.gorilla.transport.AbstractServer;
import org.myframe.gorilla.transport.MessageHandler;

import java.net.InetSocketAddress;

public class NettyServer extends AbstractServer {

	private ServerBootstrap b;

	private EventLoopGroup bossGroup = new NioEventLoopGroup(1);
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	
	private MessageHandler messageHandler;
	
	

	public NettyServer(MessageHandler messageHandler ) {
		super();
		this.messageHandler = messageHandler;
		
	}

	@Override
	public void databusRun() {
		try{
			//
			NettyChannelHandler channelHandler = new NettyChannelHandler(messageHandler);
			
			
			
			//
			b = new ServerBootstrap();
			b.childOption(ChannelOption.TCP_NODELAY, true);
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {

						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ChannelPipeline pipeline = ch.pipeline();
							pipeline.addLast("decoder", new NettyDecode(getCodec()));
							pipeline.addLast("encoder", new NettyEncode(getCodec()));
							pipeline.addLast(channelHandler);
						}
					});
			b.bind(ServicePortConfigBean.getPort()).sync();
			//
			isRun.set(true);
		}catch(Exception e){
			e.printStackTrace();
		}
		//

	}

	@Override
	public InetSocketAddress getLocalAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public InetSocketAddress getRemoteAddress() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response request(Request request) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean open() {
		databusRun();
		
		return isRun.get();
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub

	}

	@Override
	public void close(int timeout) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isClosed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public URL getUrl() {
		return null;
	}

}
