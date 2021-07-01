package org.myframe.gorilla.springsupport;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class GorillaNamespaceHandler extends NamespaceHandlerSupport {

	/**
	 * 初始化自定义注解
	 */
	@Override
	public void init() {
		registerBeanDefinitionParser("registry", new GorillaRpcBeanDefinitionParser(RegistryConfigBean.class));
		registerBeanDefinitionParser("service", new GorillaRpcBeanDefinitionParser(ServiceConfigBean.class));
		registerBeanDefinitionParser("referer", new GorillaRpcBeanDefinitionParser(RefererConfigBean.class));
		registerBeanDefinitionParser("servicePort", new GorillaRpcBeanDefinitionParser(ServicePortConfigBean.class));
	}

}
