package org.myframe.gorilla.transport.netty;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.myframe.gorilla.rpc.DefaultResponse;
import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.transport.MessageHandler;
import org.myframe.gorilla.utils.LoggerUtil;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Sharable
public class NettyChannelHandler extends SimpleChannelInboundHandler<Object> {

	// /private static Logger logger =
	// LoggerFactory.getLogger(NettyChannelHandler.class);

	private MessageHandler messageHandler;
	// private Channel serverChannel = null;

	private static Executor executor = Executors.newCachedThreadPool();

	public NettyChannelHandler(MessageHandler messageHandler) {
		this.messageHandler = messageHandler;
	}

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {

		if (msg instanceof Request) {
			processRequest(ctx, (Request) msg);
		} else if (msg instanceof Response) {
			processResponse((Response) msg);
		}

		// if (null != response) {
		// final long requestId = response.getRequestId();
		//
		// ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
		// @Override
		// public void operationComplete(ChannelFuture channelFuture) throws
		// Exception {
		// logger.info("Send response for request " + requestId);
		// }
		// });
		// }
	}

	private void processRequest(ChannelHandlerContext ctx, Request request) throws Exception {
		executor.execute(new Runnable() {

			@Override
			public void run() {
				//
				Object result = messageHandler.handle(null, request);
				DefaultResponse response = null;

				if (!(result instanceof DefaultResponse)) {
					response = new DefaultResponse(result);
				} else {
					response = (DefaultResponse) result;
				}

				response.setProcessTime(0l);

				if (ctx.channel().isActive()) {
					ctx.channel().writeAndFlush(response);
				}

			}
		});

	}

	private void processResponse(Response response) throws Exception {
		messageHandler.handle(null, response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		LoggerUtil.error("NettyChannelHandler exceptionCaught: remote=" + ctx.channel().remoteAddress() + " local="
				+ ctx.channel().localAddress() + " event=" + cause.getCause(), cause.getCause());

		ctx.channel().close();

	}

}
