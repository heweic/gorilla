package org.myframe.gorilla.test;

import java.util.List;

public interface TestService {

	
	public String sayHello(String hello) throws Exception;
	
	public String sayHello(String hello , String b);
	
	public String listTest(List<String> list);
	
	
	public List<String> listStr(String a);
	
	
	public List<String> list(List<String> list); 
	
	
	public String say3(byte[] bs);
	
}
