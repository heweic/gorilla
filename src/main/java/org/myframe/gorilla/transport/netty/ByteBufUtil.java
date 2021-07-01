package org.myframe.gorilla.transport.netty;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;

public interface ByteBufUtil {

	public Charset charset = Charset.forName("UTF-8");

	public void writeString(ByteBuf bf, String s) throws Exception;

	public void writeObject(ByteBuf bf, Object obj) throws Exception;

	public void writeInt(ByteBuf bf, int value) throws Exception;

	public void writeLong(ByteBuf bf, long value) throws Exception;

	public void writeChar(ByteBuf bf, char value) throws Exception;

	public void writeShort(ByteBuf bf, short value) throws Exception;

	public void writeByte(ByteBuf bf, byte bs) throws Exception;

	public void writeObjects(ByteBuf bf, Object[] objs) throws Exception;

	public String getString(ByteBuf bf) throws Exception;

	public int getInt(ByteBuf bf) throws Exception;

	public long getLong(ByteBuf bf) throws Exception;

	public char getChar(ByteBuf bf) throws Exception;

	public short getShort(ByteBuf bf) throws Exception;

	public byte getByte(ByteBuf bf) throws Exception;

	public Object[] getObjects(ByteBuf bf) throws Exception;

	public Object getObject(ByteBuf bf) throws Exception;
}
