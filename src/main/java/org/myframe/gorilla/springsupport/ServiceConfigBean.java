package org.myframe.gorilla.springsupport;

import org.myframe.gorilla.config.ServiceConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 服务配置对象
 *  */
public class ServiceConfigBean<T> extends ServiceConfig<T>
		implements BeanPostProcessor, ApplicationListener<ContextRefreshedEvent>, BeanFactoryAware {

	public ServiceConfigBean() {
	};

	public ServiceConfigBean(String interfaceClass, String ref) {
		super(interfaceClass, ref);
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		return bean;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		 if(event.getApplicationContext().getParent() == null){
			 if (!getExported().get()) {
					export();
				}
		 }
		

	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		super.beanFactory = beanFactory;

	}

}
