package org.myframe.gorilla.springsupport;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.config.AbstractConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * 注册中心配置bean
 */
public class RegistryConfigBean extends AbstractConfig implements InitializingBean, BeanFactoryAware, BeanNameAware {

	public RegistryConfigBean registryConfigBean;
	private String beanName;
	/**
	 * 注册中心IP
	 */
	private String ip;
	/**
	 * 注册中心端口
	 */
	private int port;

	/** spring 对象工厂 */
	private BeanFactory beanFactory;

	public String toRegistryAddress() {
		return ip + GorillaConstants.COLON_STR + port;
	}

	public RegistryConfigBean() {
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		// 内存注册配置bean
		registryConfigBean = (RegistryConfigBean) this.beanFactory.getBean(beanName);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;

	}

	@Override
	public void setBeanName(String name) {
		beanName = name;
	}

}
