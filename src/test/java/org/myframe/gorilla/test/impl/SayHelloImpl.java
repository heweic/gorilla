package org.myframe.gorilla.test.impl;

import org.myframe.gorilla.test.SayHello;

public class SayHelloImpl implements SayHello{

	@Override
	public String say(String s) {
		
		System.out.println("say hello::-->" + s);
		return s;
	}

	@Override
	public String say2(String ss) {
		return ss;
	}

	@Override
	public String say3(byte[] bs) {
	
		
		System.out.println("收到字节转String : "  + new String(bs));
		
		return new String(bs);
	}

}
