

package org.myframe.gorilla.rpc;


/**
 * 
 *  */
public interface ResponseFuture extends Future, Response {
	public void onSuccess(Response response);

	public void onFailure(Response response);

	public long getCreateTime();

	public long getTimeOut();
	
}
