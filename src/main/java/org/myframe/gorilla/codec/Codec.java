package org.myframe.gorilla.codec;

import io.netty.buffer.ByteBuf;

import java.io.IOException;

/**
 * 协议编解码
 */
public interface Codec {

	public  ByteBuf encode(io.netty.channel.Channel channel, Object message, ByteBuf bf) throws IOException;

	public Object decode(io.netty.channel.Channel channel, String remoteIp, byte[] bs, char messageType, long requestId)
			throws IOException;
}
