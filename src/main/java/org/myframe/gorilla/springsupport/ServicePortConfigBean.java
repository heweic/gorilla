package org.myframe.gorilla.springsupport;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.config.AbstractConfig;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

/**
 * 服务端口配置
 */
public class ServicePortConfigBean extends AbstractConfig implements InitializingBean, BeanFactoryAware, BeanNameAware {

	public static ServicePortConfigBean servicePortConfigBean = null;
	private String beanName;

	private BeanFactory beanFactory;

	/**
	 * 服务端口
	 */
	private int value;

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public void setBeanName(String name) {
		beanName = name;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void afterPropertiesSet() throws Exception {

		synchronized (this) {
			if (servicePortConfigBean == null) {
				servicePortConfigBean = (ServicePortConfigBean) this.beanFactory.getBean(beanName);
			}
		}
	}

	public static int getPort() {
		if (null == servicePortConfigBean || servicePortConfigBean.getValue() == 0) {
			return GorillaConstants.DEFAULT_SERVICE_PORT;
		}
		return servicePortConfigBean.getValue();
	}

}
