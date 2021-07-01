package org.myframe.gorilla.transport;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class KryoHelper {


	private Kryo kryo = new Kryo();

	public KryoHelper() {
	}

	public byte[] serialize(Object obj) throws Exception {
		Output output = new Output(512, -1);
		kryo.writeClassAndObject(output, obj);
		output.flush();
		return output.toBytes();
	}

	public Object deserialize(byte[] data) throws Exception {
		Input input = new Input(data);
		Object object = kryo.readClassAndObject(input);
		input.close();
		return object;
	}
}
