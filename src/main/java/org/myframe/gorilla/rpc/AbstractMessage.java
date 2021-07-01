package org.myframe.gorilla.rpc;

public abstract class AbstractMessage {

	
	public final static short REQUELST_TYPE = 11;
	public final static short RESPONSE_TYPE = 99;
	
	protected int messageType;

	public int getMessageType() {
		return messageType;
	}

	public void setMessageType(int messageType) {
		this.messageType = messageType;
	}

}