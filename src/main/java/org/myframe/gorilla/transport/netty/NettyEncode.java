package org.myframe.gorilla.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.myframe.gorilla.codec.Codec;

public class NettyEncode extends MessageToByteEncoder<Object> {

	private Codec codec;

	// private static Logger logger =
	// LoggerFactory.getLogger(NettyEncode.class);

	public NettyEncode(Codec codec) {
		this.codec = codec;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {

		try {
			codec.encode(ctx.channel(), msg, out);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
