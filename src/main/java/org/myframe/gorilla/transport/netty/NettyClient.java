package org.myframe.gorilla.transport.netty;

import com.esotericsoftware.minlog.Log;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.apache.commons.pool2.PooledObjectFactory;
import org.myframe.gorilla.common.ChannelState;
import org.myframe.gorilla.rpc.DefaultResponseFuture;
import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.transport.AbstractPoolClient;
import org.myframe.gorilla.transport.Channel;
import org.myframe.gorilla.transport.KryoSerialization;
import org.myframe.gorilla.transport.MessageHandler;
import org.myframe.gorilla.utils.LoggerUtil;

import java.util.Map;
import java.util.concurrent.*;

public class NettyClient extends AbstractPoolClient {

	private static ScheduledExecutorService scheduledExecutor = Executors.newScheduledThreadPool(4);

	private ScheduledFuture<?> scheduledFuture;
	private ConcurrentHashMap<Long, DefaultResponseFuture> futures = new ConcurrentHashMap<>();
	private EventLoopGroup group = new NioEventLoopGroup();
	private Bootstrap bootstrap;

	public NettyClient(URL url) {
		//
		super(url);
		//
		scheduledFuture = scheduledExecutor.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				long currentTime = System.currentTimeMillis();

				for (Map.Entry<Long, DefaultResponseFuture> entry : futures.entrySet()) {
					try {
						DefaultResponseFuture future = entry.getValue();
						if (future.getCreateTime() + future.getTimeOut() < currentTime) {
							futures.remove(entry.getKey());
							future.cancel();
						}

					} catch (Exception e) {
						LoggerUtil.error(
								" clear timeout future Error: uri=" + url.getHost() + " requestId=" + entry.getKey(),
								e);
					}

				}

			}
		}, 100, 100, TimeUnit.MILLISECONDS);
		//
	}

	public Bootstrap getBootstrap() {
		return bootstrap;
	}

	@Override
	public Response request(Request request) {
		Channel channel = null;
		Response response = null;
		try {
			channel = genericObjectPool.borrowObject();

			if(channel != null) {
				response = channel.request(request);
				//// 释放持有
				genericObjectPool.returnObject(channel);
			}

		} catch (Exception e) {
			if (channel != null) {
				try {
					// 回收
					genericObjectPool.invalidateObject(channel);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		}
		return response;
	}

	@Override
	protected PooledObjectFactory<Channel> createChannelFactory() {
		return new NettyChannelPooledObjectFactory(this);
	}

	@Override
	public boolean open() {

		if (isAvailable()) {
			return true;
		}
		//
		initClientBootstrap();
		//
		initPool();
		//
		LoggerUtil.info("NettyClient finish Open: url={}", url.getHost());
		//
		// 设置可用状态
		state = ChannelState.ALIVE;
		return state.isAliveState();
	}

	private void initClientBootstrap() {
		bootstrap = new Bootstrap();
		DefaultRpcCodec DefaultRpcCodec = new DefaultRpcCodec(new KryoSerialization());
		//
		bootstrap.group(group).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true)
				.option(ChannelOption.SO_KEEPALIVE, true).handler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws Exception {
						ChannelPipeline pipeline = ch.pipeline();
						pipeline.addLast("decoder", new NettyDecode(DefaultRpcCodec));
						pipeline.addLast("encoder", new NettyEncode(DefaultRpcCodec));
						pipeline.addLast(new NettyChannelHandler(new MessageHandler() {

							@Override
							public Object handle(org.myframe.gorilla.transport.Channel channel, Object message) {
								Response response = (Response) message;
								DefaultResponseFuture future = futures.remove(response.getRequestId());
								if (null != future) {
									// ok
									if(response.isSuccess()) {
										future.onSuccess(response);
									}else {
										future.onFailure(response);
									}
									
								}
								return null;
							}
						}));
					}
				});

	}

	@Override
	public void close() {
		if (state.isCloseState()) {
			LoggerUtil.info("NettyClient close fail: already close, url={}", url.getHost());
			return;
		}

		// 如果当前nettyClient还没有初始化，那么就没有close的理由。
		if (state.isUnInitState()) {
			LoggerUtil.info("NettyClient close Fail: don't need to close because node is unInit state: url={}",
					url.getHost());
			return;
		}
		Log.info("close client ......" + url.getAddr());
		// 清理缓存请求
		futures.clear();
		// 释放连接池
		genericObjectPool.close();
		// 停止定时清理任务
		scheduledFuture.cancel(true);
	}

	@Override
	public void close(int timeout) {
		close();
	}

	@Override
	public boolean isClosed() {
		return state.isCloseState();
	}

	@Override
	public boolean isAvailable() {
		return state.isAliveState();
	}

	public void registerCallback(long id, DefaultResponseFuture future) {
		futures.put(id, future);
	}

	@Override
	public void heartbeat(Request request) {
		try {
			request(request);
		} catch (Exception e) {
			Log.error("gorilla netty client heartbeat erro !!!! target server no response " + e.getMessage());
		}
	}

}
