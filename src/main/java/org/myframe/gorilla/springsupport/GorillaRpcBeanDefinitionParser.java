package org.myframe.gorilla.springsupport;

import org.myframe.gorilla.config.AbstractConfig;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

public class GorillaRpcBeanDefinitionParser implements BeanDefinitionParser {

	private Class<?> beanClass;
	private boolean required = true;

	public GorillaRpcBeanDefinitionParser(Class<?> beanClass) {
		this.beanClass = beanClass;
	}

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {

		RootBeanDefinition bd = new RootBeanDefinition();
		bd.setBeanClass(beanClass);
		bd.setLazyInit(false);

		String id = element.getAttribute("id");
		if ((id == null || id.length() == 0) && required) {
			String generatedBeanName = element.getAttribute("name");
			if (generatedBeanName == null || generatedBeanName.length() == 0) {
				generatedBeanName = element.getAttribute("class");
			}
			if (generatedBeanName == null || generatedBeanName.length() == 0) {
				generatedBeanName = beanClass.getName();
			}
			id = generatedBeanName;
			int counter = 2;
			while (parserContext.getRegistry().containsBeanDefinition(id)) {
				id = generatedBeanName + (counter++);
			}
		}
		if (id != null && id.length() > 0) {
			if (parserContext.getRegistry().containsBeanDefinition(id)) {
				throw new IllegalStateException("Duplicate spring bean id " + id);
			}
			parserContext.getRegistry().registerBeanDefinition(id, bd);
		}
		bd.getPropertyValues().addPropertyValue("id", id);

		//

		if (beanClass.equals(RefererConfigBean.class)) {
			//
			setReferer(bd, element);
			//
			return bd;
		} else if (beanClass.equals(RegistryConfigBean.class)) {
			//

			setRegistry(bd, element);
			//
			return bd;
		} else if (beanClass.equals(ServiceConfigBean.class)) {

			setService(bd, element);

		} else if (beanClass.equals(ServicePortConfigBean.class)) {
			setPort(bd, element);
		}
		return bd;

	}

	private void setPort(RootBeanDefinition bd, Element element) {
		bd.getPropertyValues().add("value", element.getAttribute("value"));
		AbstractConfig.providerPort = Integer.parseInt(element.getAttribute("value"));
	}

	private String setReferer(RootBeanDefinition bd, Element element) {
		String id = element.getAttribute("id");
		String interfaceClass = element.getAttribute("interface");
		try {
			bd.getPropertyValues().add("interfaceClass", Class.forName(interfaceClass));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		if (element.hasAttribute("diyNode")) {
			String diyNode = element.getAttribute("diyNode");
			bd.getPropertyValues().add("diyNode", diyNode);
		}

		//
		RefererConfigBean.ids.add(id);
		return id;
	}

	private void setRegistry(RootBeanDefinition bd, Element element) {
		bd.getPropertyValues().add("ip", element.getAttribute("ip"));
		bd.getPropertyValues().add("port", element.getAttribute("port"));
		RegistryConfigBean bean = new RegistryConfigBean();
		bean.setIp(element.getAttribute("ip"));
		bean.setPort(Integer.parseInt(element.getAttribute("port")));
		AbstractConfig.configBean = bean;
	}

	private void setService(RootBeanDefinition bd, Element element) {
		String interfaceClass = element.getAttribute("interface");
		String ref = element.getAttribute("ref");
		bd.getPropertyValues().add("interfaceClass", interfaceClass);
		bd.getPropertyValues().add("ref", ref);
		ServiceConfigBean.service.add(new ServiceConfigBean<>(interfaceClass, ref));
	}

}
