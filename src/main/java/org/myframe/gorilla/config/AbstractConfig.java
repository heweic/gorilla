package org.myframe.gorilla.config;

import org.myframe.gorilla.common.GorillaConstants;
import org.myframe.gorilla.springsupport.RegistryConfigBean;

/**
 * 抽象配置
 */
public abstract class AbstractConfig {

	/**
	 * 注册中心
	 */
	public static RegistryConfigBean configBean;
	/**
	 * 默认服务端口
	 */
	public static int providerPort = GorillaConstants.DEFAULT_SERVICE_PORT;

	public static int MANAGERPORT;
	
	
	/**
	 * 配置bean ID
	 */
	protected String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
