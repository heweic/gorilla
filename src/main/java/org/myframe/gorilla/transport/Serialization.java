package org.myframe.gorilla.transport;

import org.myframe.gorilla.extension.Scope;
import org.myframe.gorilla.extension.Spi;

@Spi(scope = Scope.SINGLETON)
public interface Serialization {

	/** object to byte[] */
	public byte[] serialize(Object obj) throws Exception;

	/** byte[] to object */
	public Object deserialize(byte[] data) throws Exception;
}
