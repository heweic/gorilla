package org.myframe.gorilla.test;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.extension.ExtensionLoader;
import org.myframe.gorilla.transport.EndpointFactory;

public class TEst {

	
	public static void main(String[] args) {
		EndpointFactory factory = ExtensionLoader.getExtensionLoader(EndpointFactory.class)
				.getExtension(GorillaConstants.DEFAULT_VALUE);
		
		
		System.out.println(factory.hashCode());
		
		
		Thread t = new Thread( ()->{
			EndpointFactory factory1 = ExtensionLoader.getExtensionLoader(EndpointFactory.class)
					.getExtension(GorillaConstants.DEFAULT_VALUE);
			
			System.out.println(factory1.hashCode());
		});
		
		t.start();
		
	}
}
