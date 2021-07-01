package org.myframe.gorilla.transport;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.SpiMeta;
import org.myframe.gorilla.utils.LoggerUtil;

@SpiMeta(name = GorillaConstants.DEFAULT_VALUE)
public class KryoSerialization implements Serialization {

	
	/** ThreadLocal kryoHelper instance */
	private static ThreadLocal<KryoHelper> threadLocalKryo = new ThreadLocal<KryoHelper>() {
		protected KryoHelper initialValue() {
			return new KryoHelper();
		};
	};

	public byte[] serialize(Object obj) {
		if (obj == null) {
			return new byte[0];
		}
		try {

			return threadLocalKryo.get().serialize(obj);
		} catch (Exception e) {
			LoggerUtil.error("KryoSerialization serialize erro ! class: " + obj.getClass().getName() + "" + "exception "
					+ e.getMessage());
		}
		return new byte[0];
	}

	public Object deserialize(byte[] data) {
		try {
			return threadLocalKryo.get().deserialize(data);
		} catch (Exception e) {
			LoggerUtil.error("KryoSerialization deserialize erro !" + e.getMessage());
		}
		return new byte[0];
	}

}
