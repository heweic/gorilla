package org.myframe.gorilla.transport;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.common.ResponseStatFactory;
import org.myframe.gorilla.extension.SpiMeta;
import org.myframe.gorilla.rpc.DefaultRequest;
import org.myframe.gorilla.rpc.DefaultResponse;
import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.utils.ReflectUtil;
import org.myframe.gorilla.utils.RequestIdGenerator;

@SpiMeta(name = GorillaConstants.DEFAULT_VALUE)
public class DefaultHeartbeatFactory implements HeartbeatFactory {

	private static final Object[] args = new Object[0];

	private static final String methodName = "heartbeat";
	public static final String EMPTY_PARAM = ReflectUtil.EMPTY_PARAM;
	private static final String interfaceName = "com.myframe.gorilla.rpc.heartbeat";

	@Override
	public Request createRequest() {
		DefaultRequest request = new DefaultRequest();

		request.setRequestId(RequestIdGenerator.getRequestId());
		request.setArguments(args);
		request.setMethodName(methodName);
		request.setParamtersDesc(EMPTY_PARAM);
		request.setInterfaceName(interfaceName);
		return request;
	}

	@Override
	public MessageHandler wrapMessageHandler(MessageHandler handler) {
		return new HeartMessageHandleWrapper(handler);
	}

	/**
	 * 如果是响应返回 -1
	 *  */
	public  Object heartbeat(Object message) {
		if ( !(message instanceof Request) ) {
			//如果不是请求，返回null
			return  null;
		}

		Request request = (Request) message;

		return interfaceName.equals(request.getInterfaceName()) && methodName.equals(request.getMethodName())
				&& EMPTY_PARAM.endsWith(request.getParamtersDesc()) ? heartbeatResponse(request.getRequestId()) : null ;
	}

	
	public static Response heartbeatResponse(long id){
		DefaultResponse response = new DefaultResponse();
		response.setValue(ResponseStatFactory.valueVOID());
		response.setRequestId(id);
		response.setExceptionNone();
		response.setProcessTime(-1L);
		return response;
	}
	
	private class HeartMessageHandleWrapper implements MessageHandler {
		private MessageHandler messageHandler;

		public HeartMessageHandleWrapper(MessageHandler messageHandler) {
			this.messageHandler = messageHandler;
		}

		@Override
		public Object handle(Channel channel, Object message) {

			//
			Object obj = heartbeat(message);
			if (null != obj) {
				return obj;
			}
			//
			obj =  messageHandler.handle(channel, message);
			return obj;
		}

	}
}
