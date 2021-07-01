
package org.myframe.gorilla.cluster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.exception.BaseException;
import org.myframe.gorilla.extension.SpiMeta;
import org.myframe.gorilla.rpc.Referer;
import org.myframe.gorilla.rpc.Request;
import org.myframe.gorilla.rpc.Response;
import org.myframe.gorilla.rpc.URL;
import org.myframe.gorilla.utils.CollectionUtil;

@SpiMeta(name = GorillaConstants.DEFAULT_VALUE)
public class DefaultCluster<T> implements Cluster<T> {

	/**
	 * 接口实现应用集合
	 */
	private List<Referer<T>> referers;

	private AtomicBoolean available = new AtomicBoolean(false);
	private URL url;

	private Router<T> router;

	public DefaultCluster() {
		router = new RoundRobinRouter<>();
	}

	public DefaultCluster(Router<T> router) {
		super();
		this.router = router;
	}

	@Override
	public Response call(Request request) throws Exception {
		if (!this.available.get()) {
			throw new BaseException("DefaultCluster unavailable !!!!");
		}

		if (null == referers) {
			throw new BaseException("referers is null  !!!!");
		}

		if (referers.size() == 0) {
			throw new BaseException("no referer to request  !!!!");
		}
		if (referers.size() == 1) {
			return referers.get(0).call(request);
		} else {
			// do rpc call

			Referer<T> referer = doSelect();
			if (null != referer) {
				return referer.call(request);
			}
		}
		throw new Exception("no referers..");
	}

	private Referer<T> doSelect() {
		return router.doWwitch(referers);
	}

	@Override
	public void onRefresh(List<Referer<T>> referers) {
		if (CollectionUtil.isEmpty(referers)) {
			return;
		}
		//
		this.referers = referers;

		//
	}

	@Override
	public void addReferer(Referer<T> referer) {
		if(null == referers) {
			referers = new ArrayList<>();
		}
		//
		this.referers.add(referer);
	}

	@Override
	public Class<T> getInterface() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		available.set(true);
	}

	@Override
	public void destroy() {

	}

	@Override
	public boolean isAvailable() {
		return available.get();
	}

	@Override
	public String desc() {
		if (null != this.url) {
			return this.url.toString();
		}
		return null;
	}

	@Override
	public URL getUrl() {
		return this.url;
	}

	@Override
	public void setUrl(URL url) {
		this.url = url;
	}

}
