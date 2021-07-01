package org.myframe.gorilla.transport.netty;

import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import org.myframe.gorilla.codec.Codec;
import org.myframe.gorilla.common.GorillaConstants;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;

public class NettyDecode extends ByteToMessageDecoder {

	private Codec codec;

	// private static Logger logger =
	// LoggerFactory.getLogger(NettyDecode.class);

	public NettyDecode(Codec codec) {
		this.codec = codec;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

		if (in.readableBytes() <= GorillaConstants.NETTY_HEADER) {
			return ;
		}
		
		in.markReaderIndex();
		
		short type = in.readShort();
		if (type != GorillaConstants.NETTY_MAGIC_TYPE) {
			in.resetReaderIndex();
			ctx.channel().close();
			return;
		}
		// read head
		char messageType = in.readChar();
		long requestId = in.readLong();
		int dataLength = in.readInt();
		
		if (in.readableBytes() < dataLength) {
			in.resetReaderIndex();
			return;
		}
		// remote IP address
		String remoteIp = getRemoteIp(ctx.channel());
		
		// read body
		byte[] bs = new byte[dataLength];
		in.readBytes(bs);
		//
		
		out.add(codec.decode(ctx.channel(), remoteIp, bs, messageType, requestId));
	}

	private String getRemoteIp(Channel channel) {

		String ip = GorillaConstants.EMPTY_STR;
		SocketAddress remote = channel.remoteAddress();
		if (remote != null) {
			try {

				ip = ((InetSocketAddress) remote).getAddress().getHostAddress();

			} catch (Exception e) {

			}
		}
		return ip;

	}

}
