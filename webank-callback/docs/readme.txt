使用说明

1.先启动diamond项目
10.160.41.69 a.b.c
10.160.40.202 d.e.f


2.配置动态配置：
【dataId=mysql  group=webank_callback】
#数据库配置
dataSource.driverClassName=com.mysql.jdbc.Driver
dataSource.url=jdbc:mysql://localhost:3306/webank-callback
dataSource.username = root
dataSource.password = 123456
dataSource.initialSize  =  2
dataSource.maxActive = 30
dataSource.maxIdle = 2
dataSource.minIdle = 2
dataSource.maxOpenPreparedStatements = 150
dataSource.validationQuery = SELECT 1 FROM DUAL
dataSource.testWhileIdle = true
dataSource.testOnBorrow = false
dataSource.testOnReturn = false
dataSource.timeBetweenEvictionRunsMillis = 60000
dataSource.minEvictableIdleTimeMillis = 300000
dataSource.poolPreparedStatements = true
dataSource.maxPoolPreparedStatementPerConnectionSize = 20
dataSource.filters = stat



【dataId=common group=webank_callback】
#Dubbo配置
dubbo.app.name=webank-callback-service
dubbo.app.port=60880
dubbo.app.address=zookeeper://172.16.1.120:2181?backup=172.16.1.121:2181,172.16.1.122:2181,172.16.1.123:2181

#发送邮件
mail.host=smtp.exmail.qq.com
mail.port=25
mail.username=admin@dtds.com.cn
mail.password=admin
	

