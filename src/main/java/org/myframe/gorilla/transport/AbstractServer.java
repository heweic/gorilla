package org.myframe.gorilla.transport;

import org.myframe.gorilla.codec.Codec;
import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.transport.netty.DefaultRpcCodec;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * 服务对象抽象类
 */
public abstract class AbstractServer implements Server {

	protected AtomicBoolean isRun = new AtomicBoolean(false);

	private Codec codec;
	private Serialization serialization;

	@Override
	public void init() throws Exception {

		//
		this.serialization = ExtensionLoader.getExtensionLoader(Serialization.class)
				.getExtension(GorillaConstants.DEFAULT_VALUE);
		this.codec = new DefaultRpcCodec(serialization);
		//
		databusRun();
	}

	public abstract void databusRun() throws Exception;

	public Codec getCodec() {
		return codec;
	}

	public Serialization getSerialization() {
		return serialization;
	}

}
