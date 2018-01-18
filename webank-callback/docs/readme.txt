使用说明

1.是否启动diamond
	127.0.0.1 a.b.c
	127.0.0.1 d.e.f
	是否启用diamond分布式动态配置：applicationContext-service.xml里选择哪种：加载动态配置方式1
	动态配置:
		zookeeper
		common
		mysql
		
		有依赖以前淘实惠的两个jar
		<dependency>
			<groupId>com.zhc</groupId>
			<artifactId>diamond-utils</artifactId>
			<version>2.0.5.4</version>
		</dependency>
		<dependency>
			<groupId>com.zhc</groupId>
			<artifactId>diamond-client</artifactId>
			<version>2.0.5.4</version>
		</dependency>	


2.访问： http://localhost/webank/notify.do
