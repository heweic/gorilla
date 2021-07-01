package org.myframe.gorilla.utils;

import org.myframe.gorilla.common.GorillaConstants;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.nio.charset.Charset;

public class ZkUtils {

	public static final Charset default_charset = Charset.forName("utf-8");
	
	public static String toPath(String interfaceName) {
		return GorillaConstants.ZK_ROOT_PATH + GorillaConstants.BACKSLASH_STR + interfaceName;
	}

	public static String toPath(String intefaceName, String providerAddr) {
		return GorillaConstants.ZK_ROOT_PATH + GorillaConstants.BACKSLASH_STR + intefaceName
				+ GorillaConstants.BACKSLASH_STR + providerAddr;
	}

	public static String toValue(String host, int port)  {
		return host + GorillaConstants.COLON_STR + port;
	}
	
	public static String toString(byte[] bs ) throws Exception{
		return new String(bs, default_charset);
	}
	
	public static String getAddr(String source) throws Exception{
		return source.split( GorillaConstants.COLON_STR)[0];
	}
	public static int getPort(String source) throws Exception{
		return Integer.parseInt(source.split( GorillaConstants.COLON_STR)[1]);
	}
	
	public static byte[] toBytes(String value) throws Exception{
		return value.getBytes(default_charset);
	}
	
	public static void main(String[] args) {
		ByteBuf buf = Unpooled.buffer(2);
	
		buf.writeChar(Integer.parseInt("0019", 16));
		
		System.out.println(buf.getByte(0));
		System.out.println(buf.getByte(1));
	}
}
