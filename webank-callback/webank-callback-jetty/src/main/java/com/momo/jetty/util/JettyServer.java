package com.momo.jetty.util;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.eclipse.jetty.server.Server;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

/**
 * 适用于servlet3.0+jetty8，本机开发使用。
 */
public class JettyServer {

	/**
	 * 启动Jetty.
	 * 
	 * @param projectName
	 *            项目名称
	 * @return
	 * @throws Exception
	 */
	public static Server start() throws Exception {
		return start(80);
	}

	public static Server start(int port) throws Exception {
		if (SystemUtil.isNotWindows()) {
			if (port == 80) {
				port = 8080;
			}
		}
		Server server = start("/", port);
		return server;
	}

	public static Server start(String contextPath, int port) throws Exception {
		System.setProperty(AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME, "jetty");

		Server server = buildNormalServer(contextPath, port);
		server.start();
		return server;
	}

	public static Server buildNormalServer(String contextPath, int port) {
		JettyUtil.setJetty(true);

		//		String project = AppProperties.getProjectName();
		//		EnvUtil.setProjectName(project);
		//		{
		//			new ConfigFileChecker(project).start();// 相关配置文件检查
		//		}

		useDefaultLog4jProperties();// 使用默认的log4.properties配置
		return BuildServer.buildNormalServer(port, contextPath);
	}

	/**
	 * 使用默认的log4.properties配置
	 */
	protected static void useDefaultLog4jProperties() {
		Resource resource = new ClassPathResource("log4j2.xml");
		boolean hasLog4jProperties = (resource != null && resource.exists());
		if (!hasLog4jProperties) {
			loadLog4j();
		}
	}

	protected static void loadLog4j() {
		boolean hasLog4j = hasLog4j();
		if (hasLog4j) {
			try {
				System.err.println("你还没有自定义log4j.properties，当前使用的日志配置是Leopard默认的，只在JettyTest环境有效.");
				Resource defaultResource = new ClassPathResource("/jetty/log4j.properties");
				PropertyConfigurator.configure(defaultResource.getInputStream());
			} catch (IOException e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
	}

	protected static boolean hasLog4j() {
		boolean hasLog4j;
		try {
			Class.forName("org.apache.log4j.PropertyConfigurator");
			hasLog4j = true;
		} catch (ClassNotFoundException e) {
			hasLog4j = false;
		}
		return hasLog4j;
	}

}
