package com.momo.jetty.web;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 配置文件检查.
 * 
 * 
 */
public class ConfigFileChecker {

	private final String projectName;

	public ConfigFileChecker(String projectName) {
		this.projectName = projectName;
	}

	protected void checkApplicationContext() {
		Resource resource = new ClassPathResource("/applicationContext.xml");
		if (resource == null || !resource.exists()) {
			System.err.println("[配置检查] - 文件[src/main/resources/applicationContext.xml]不存在.");
		}
	}

	// udbsdk.properties

	protected void checkApplicationDaoContext() {
		Resource resource = new ClassPathResource("/applicationContext-dao.xml");
		if (resource == null || !resource.exists()) {
			System.err.println("[配置检查] - 文件[" + projectName + "-dao/src/main/resources/applicationContext-dao.xml]不存在.");
		}
	}

	protected void checkApplicationServiceContext() {
		Resource resource = new ClassPathResource("/applicationContext-service.xml");
		if (resource == null || !resource.exists()) {
			System.err.println("[配置检查] - 文件[" + projectName + "-service/src/main/resources/applicationContext-service.xml]不存在.");
		}
	}

	protected void checkJspDir() {
	}

	public void start() {
		this.checkApplicationContext();
		this.checkApplicationServiceContext();
		this.checkApplicationDaoContext();
		this.checkJspDir();
	}
}
