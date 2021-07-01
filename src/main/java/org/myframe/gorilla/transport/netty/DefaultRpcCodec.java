package org.myframe.gorilla.transport.netty;

import java.io.IOException;

import org.myframe.gorilla.codec.Codec;
import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.rpc.AbstractMessage;
import org.myframe.gorilla.rpc.DefaultRequest;
import org.myframe.gorilla.rpc.DefaultResponse;
import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.transport.Serialization;
import org.myframe.gorilla.utils.LoggerUtil;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.util.ReferenceCountUtil;

public class DefaultRpcCodec extends GorillaByteBufUtil implements Codec {

	public DefaultRpcCodec(Serialization serialization) {
		super(serialization);
	}

	@Override
	public ByteBuf encode(Channel channel, Object message, ByteBuf bf) throws IOException {

		// tag
		bf.writeShort(GorillaConstants.NETTY_MAGIC_TYPE);

		//
		if (message instanceof Request) {
			try {
				requestEncode(channel, message, bf);
			} catch (Exception e) {
				LoggerUtil.error("请求编码失败.. " + e.getMessage());
			}
		} else if (message instanceof Response) {
			try {
				responseEncode(message, bf);
			} catch (Exception e) {
				LoggerUtil.error("响应编码失败.." + e.getMessage());
			}
		} else if (message instanceof String) {
			bf.writeBytes(((String) message).getBytes());
		}
		return bf;
	}

	/**
	 * 请求编码
	 */
	private void requestEncode(Channel channel, Object message, ByteBuf fullBf) throws Exception {
		Request request = (Request) message;
		// 头信息
		fullBf.writeShort(AbstractMessage.REQUELST_TYPE);
		fullBf.writeLong(request.getRequestId());
		// 消息体
		ByteBuf body = Unpooled.buffer();
		try {

			writeString(body, request.getInterfaceName());
			writeString(body, request.getMethodName());
			writeString(body, request.getParamtersDesc());
			writeObjects(body, request.getArguments());

			fullBf.writeInt(body.readableBytes());
			fullBf.writeBytes(body);
		} finally {
			ReferenceCountUtil.release(body);
		}

	};

	/**
	 * 响应编码
	 */
	private void responseEncode(Object message, ByteBuf fullBf) throws Exception {
		//
		Response response = (Response) message;
		// 头信息
		fullBf.writeShort(AbstractMessage.RESPONSE_TYPE);
		fullBf.writeLong(response.getRequestId());
		// 消息体
		ByteBuf body = Unpooled.buffer();
		try {

			writeLong(body, response.getProcessTime());
			writeObject(body, response.getValue());
			writeObject(body, response.getException());

			fullBf.writeInt(body.readableBytes());
			fullBf.writeBytes(body);
		} finally {
			ReferenceCountUtil.release(body);
		}
	};

	@Override
	public Object decode(Channel channel, String remoteIp, byte[] bs, char messageType, long requestId)
			throws IOException {
		//
		ByteBuf buffer = Unpooled.copiedBuffer(bs);

		try {
			switch (messageType) {
			// 解码请求包
			case AbstractMessage.REQUELST_TYPE:

				DefaultRequest request = new DefaultRequest();
				try {
					reuqestDecode(buffer, request, messageType, requestId);
				} catch (Exception e) {
					LoggerUtil.error("解码请求包失败.." + e.getMessage());
				}

				return request;
			// 解码响应包
			case AbstractMessage.RESPONSE_TYPE:

				DefaultResponse response = new DefaultResponse();
				try {
					responseDecode(buffer, response, messageType, requestId);
				} catch (Exception e) {
					LoggerUtil.error("解码响应包失败.." + e.getMessage());
				}

				return response;
			default:
				return null;
			}
		} finally {
			ReferenceCountUtil.release(buffer);
		}

	}

	/**
	 * 请求解码
	 */
	private void reuqestDecode(ByteBuf buffer, DefaultRequest defaultRequest, char messageType, long requestId)
			throws Exception {

		// 设置头
		defaultRequest.setMessageType(messageType);
		defaultRequest.setRequestId(requestId);

		// 设置体
		String interfaceName = getString(buffer);
		String methodName = getString(buffer);
		String paramtersDesc = getString(buffer);
		Object[] args = getObjects(buffer);

		defaultRequest.setInterfaceName(interfaceName);
		defaultRequest.setMethodName(methodName);
		defaultRequest.setParamtersDesc(paramtersDesc);
		defaultRequest.setArguments(args);
	};

	/** 响应解码 */
	private void responseDecode(ByteBuf buffer, DefaultResponse defaultResponse, char messageType, long requestId)
			throws Exception {

		// 设置头
		defaultResponse.setMessageType(messageType);
		defaultResponse.setRequestId(requestId);

		// 设置体
		long processTime = getLong(buffer);
		Object value = getObject(buffer);
		Object exception = getObject(buffer);

		defaultResponse.setException(null == exception ? null : (Exception) exception);
		defaultResponse.setValue(value);
		defaultResponse.setProcessTime(processTime);
	};

}
