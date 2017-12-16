package com.momo.test;

import org.junit.Ignore;

import com.momo.jetty.util.JettyServer;

/**
 * WEB启动测试类
 */
@Ignore
public class JettyTest {

	/**
	 * WEB启动程序
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		//System.setProperty("DTPROJECTNO", "demo");
		//System.setProperty("DTENV", "dev");
		JettyServer.start(8080);
	}

}
