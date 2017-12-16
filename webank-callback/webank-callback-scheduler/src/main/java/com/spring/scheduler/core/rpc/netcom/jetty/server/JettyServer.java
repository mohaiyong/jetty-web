package com.spring.scheduler.core.rpc.netcom.jetty.server;

import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.nio.SelectChannelConnector;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.scheduler.core.thread.ExecutorRegistryThread;
import com.spring.scheduler.core.thread.TriggerCallbackThread;

/**
 * ClassName: JettyServer 
 * @Description: rpc jetty server
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class JettyServer {
	private static final Logger logger = LoggerFactory.getLogger(JettyServer.class);

	private Server server;
	private Thread thread;
	public void start(final int port, final String ip, final String client, final String clientName) throws Exception {
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
/*************************** jetty9  *********************************/
//				//初始jetty服务对象
//				server = new Server(new ExecutorThreadPool());
//
//				//初始服务连接
//				ServerConnector connector = new ServerConnector(server);
//				connector.setPort(port);
//				server.setConnectors(new Connector[]{connector});
				Server server = new Server();// 创建jetty web容器
		        server.setStopAtShutdown(true);// 在退出程序是关闭服务
		        /*************************** jetty8 支持jdk1.6 **********************************/
		        // 创建连接器，每个连接器都是由IP地址和端口号组成，连接到连接器的连接将会被jetty处理
		        Connector connector = new SelectChannelConnector();// 创建一个连接器
		        connector.setHost(ip);// ip地址
		        connector.setPort(port);// 连接的端口号
		        server.addConnector(connector);// 添加连接
		        QueuedThreadPool threadPool = new QueuedThreadPool();
		        threadPool.setMaxThreads(3000);
		        server.setThreadPool(threadPool);

				//设置执行程序
				HandlerCollection handlerc =new HandlerCollection();
				handlerc.setHandlers(new Handler[]{new JettyServerHandler()});
				server.setHandler(handlerc);

				try {
					//启动服务
					server.start();
					logger.info(">>>>>>>>>>>>  jetty server start success at port:{}.", port);

					//注册服务
					ExecutorRegistryThread.getInstance().start(port, ip, client, clientName);

					//回调服务
					TriggerCallbackThread.getInstance().start();
					
					//阻塞直到线程停止ֹ
					server.join();
					logger.info(">>>>>>>>>>> xxl-rpc server join success, netcon={}, port={}", JettyServer.class.getName(), port);
				} catch (Exception e) {
					logger.error("", e);
				} finally {
					destroy();
				}
			}
		});
		thread.setDaemon(true);	// daemon, service jvm, user thread leave >>> daemon leave >>> jvm leave
		thread.start();
	}

	public void destroy() {

		// destroy server
		if (server != null) {
			try {
				server.stop();
				server.destroy();
			} catch (Exception e) {
				logger.error("", e);
			}
		}
		if (thread.isAlive()) {
			thread.interrupt();
		}

		// destroy Registry-Server
		ExecutorRegistryThread.getInstance().toStop();

		// destroy Callback-Server
		TriggerCallbackThread.getInstance().toStop();

		logger.info(">>>>>>>>>>> server destroy success, netcon={}", JettyServer.class.getName());
	}

}
