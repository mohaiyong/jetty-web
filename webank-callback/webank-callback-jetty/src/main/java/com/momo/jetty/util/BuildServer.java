package com.momo.jetty.util;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.FragmentConfiguration;
import org.eclipse.jetty.webapp.WebAppClassLoader;
import org.eclipse.jetty.webapp.WebAppContext;

public class BuildServer {
	private static Log logger = LogFactory.getLog(BuildServer.class);

	protected static boolean isOpened(int port) {
		try {
			Socket s = new Socket();
			s.bind(new InetSocketAddress("0.0.0.0", port));
			s.close();
			return false;
		} catch (java.net.BindException e) {
			String message = e.getMessage();
			if ("权限不够".equals(message)) {
				throw new RuntimeException("您无权限绑定" + port + "端口");
			}
			e.printStackTrace();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	/**
	 * 创建用于正常运行调试的Jetty Server, 以src/main/webapp为Web应用目录.
	 */
	public static Server buildNormalServer(int port, String contextPath) {

		boolean isOpened = isOpened(port);
		if (isOpened) {
			throw new RuntimeException("端口[" + port + "]已被占用.");
		}

		Server server = new Server(port);
		WebAppContext webContext = new WebAppContext("src/main/webapp", contextPath);
		webContext.setDefaultsDescriptor("/jetty/webdefault.xml");

		// 问题点：http://stackoverflow.com/questions/13222071/spring-3-1-webapplicationinitializer-embedded-jetty-8-annotationconfiguration
		webContext.setConfigurations(new Configuration[] { //
		new EmbedWebInfConfiguration()//
				, new EmbedWebXmlConfiguration()//
				, new EmbedMetaInfConfiguration()//
				, new FragmentConfiguration()//
				, new EmbedAnnotionConfiguration() //
		});

		WebAppClassLoader classLoader = null;
		try {
			// addTldLib(webContext);
			classLoader = new WebAppClassLoader(webContext);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ClassLoader tldClassLoader = addTldLib(classLoader);
		webContext.setClassLoader(classLoader);

		webContext.setParentLoaderPriority(true);
		logger.debug(webContext.dump());
		server.setHandler(webContext);
		server.setStopAtShutdown(true);

		return server;
	}

}
