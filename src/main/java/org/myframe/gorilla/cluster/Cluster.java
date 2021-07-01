package org.myframe.gorilla.cluster;

import org.myframe.gorilla.extension.Scope;
import org.myframe.gorilla.extension.Spi;
import org.myframe.gorilla.rpc.Caller;
import org.myframe.gorilla.rpc.Referer;
import org.myframe.gorilla.rpc.URL;

import java.util.List;

/**
 * 调用串 RPC 客服端接口
 */
@Spi(scope = Scope.PROTOTYPE)
public interface Cluster<T> extends Caller<T> {

	@Override
	public void init();

	/** */
	public void setUrl(URL url);

	/**
	 * 
	 *  */
	public void onRefresh(List<Referer<T>> list);

	public void addReferer(Referer<T> referer);
}
