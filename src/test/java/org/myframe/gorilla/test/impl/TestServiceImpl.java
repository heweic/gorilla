package org.myframe.gorilla.test.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.myframe.gorilla.test.TestService;

public class TestServiceImpl implements TestService {

	private static AtomicInteger atomicInteger = new AtomicInteger(0);
	
	@Override
	public String sayHello(String hello) throws Exception{
		
		return null;
	}

	@Override
	public String sayHello(String hello, String b) {
		
		
		
		return "OK B !!!!!";
	}

	@Override
	public List<String> listStr(String a) {
		List<String> list = new ArrayList<>();
		
		return list;
	}

	@Override
	public String listTest(List<String> list) {
		System.out.println("收到请求开始阻塞.... : ->" + atomicInteger.incrementAndGet());
		
		return list.size() + "";
	}

	@Override
	public List<String> list(List<String> list) {
		//throw new GorillaServiceException(" idy excepton");
		return list;
	}

	@Override
	public String say3(byte[] bs) {		
		return new String(bs);
	}


}
