package org.myframe.gorilla.transport.netty;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.transport.Serialization;

import io.netty.buffer.ByteBuf;

public class GorillaByteBufUtil implements ByteBufUtil {

	private Serialization serialization;

	public GorillaByteBufUtil(Serialization serialization) {
		this.serialization = serialization;
	}

	@Override
	public void writeString(ByteBuf bf, String s) {
		if (null == s) {
			bf.writeInt(0);
			return;
		}
		byte[] bs = s.getBytes();
		bf.writeInt(bs.length);
		bf.writeBytes(bs);
	}

	@Override
	public String getString(ByteBuf bf) throws Exception {
		int length = bf.readInt();
		if (length == 0) {
			return GorillaConstants.EMPTY_STR;
		}
		byte[] bs = new byte[length];
		bf.readBytes(bs);
		return new String(bs, charset);
	}

	@Override
	public void writeObject(ByteBuf bf, Object obj) throws Exception {
		if (obj == null) {
			bf.writeInt(0);
			return;
		}
		byte[] bs = serialization.serialize(obj);
		bf.writeInt(bs.length);
		bf.writeBytes(bs);
	}

	@Override
	public Object getObject(ByteBuf bf) throws Exception {
		int length = bf.readInt();
		if (0 == length) {
			return null;
		}
		byte[] bs = new byte[length];
		bf.readBytes(bs);
		return serialization.deserialize(bs);
	}

	@Override
	public void writeInt(ByteBuf bf, int value) {
		bf.writeInt(value);
	}

	@Override
	public int getInt(ByteBuf bf) throws Exception {
		return bf.readInt();
	}

	@Override
	public void writeLong(ByteBuf bf, long value) {
		bf.writeLong(value);
	}

	@Override
	public long getLong(ByteBuf bf) throws Exception {
		return bf.readLong();
	}

	@Override
	public void writeChar(ByteBuf bf, char value) {
		bf.writeChar(value);
	}

	@Override
	public char getChar(ByteBuf bf) throws Exception {
		return bf.readChar();
	}

	@Override
	public void writeShort(ByteBuf bf, short value) {
		bf.writeShort(value);
	}

	@Override
	public short getShort(ByteBuf bf) throws Exception {
		return bf.readShort();
	}

	@Override
	public void writeByte(ByteBuf bf, byte bs) {
		bf.writeByte(bs);
	}

	@Override
	public byte getByte(ByteBuf bf) throws Exception {
		return bf.readByte();
	}

	@Override
	public void writeObjects(ByteBuf bf, Object[] objs) throws Exception {
		if (objs == null) {
			writeChar(bf, (char) 0);
			return;
		}
		writeChar(bf, (char) objs.length);
		for (int i = 0; i < objs.length; i++) {
			this.writeObject(bf, objs[i]);
		}
	}

	@Override
	public Object[] getObjects(ByteBuf bf) throws Exception {
		char length = getChar(bf);
		if (0 == length) {
			return new Object[length];
		}
		Object[] objs = new Object[length];
		for (int i = 0; i < length; i++) {
			objs[i] = getObject(bf);
		}
		return objs;
	}

}
