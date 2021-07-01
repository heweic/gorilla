package org.myframe.gorilla.rpc;

public class DefaultRequest extends AbstractMessage implements Request {

	private String interfaceName;
	private String methodName;
	private String paramtersDesc;
	private Object[] arguments;
	private long requestId;


	
	@Override
	public Object[] getArguments() {
		return arguments;
	}

	@Override
	public String getInterfaceName() {
		return interfaceName;
	}

	@Override
	public String getMethodName() {
		return methodName;
	}

	@Override
	public String getParamtersDesc() {
		return paramtersDesc;
	}
	@Override
	public long getRequestId() {
		return requestId;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public void setParamtersDesc(String paramtersDesc) {
		this.paramtersDesc = paramtersDesc;
	}

	public void setArguments(Object[] arguments) {
		this.arguments = arguments;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public String toString() {
		return interfaceName + "." + methodName + "(" + paramtersDesc + ") requestId=" + requestId;
	}

	@Override
	public String info() {
		return toString();
	}



}
