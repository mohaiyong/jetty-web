ʹ��˵��

1.������diamond��Ŀ

2.���ö�̬���ã�
��dataId=mysql  group=webank_callback��
#���ݿ�����
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



��dataId=common group=webank_callback��
#Dubbo����
dubbo.app.name=webank-callback-service
dubbo.app.port=60880
dubbo.app.address=zookeeper://172.16.1.120:2181?backup=172.16.1.121:2181,172.16.1.122:2181,172.16.1.123:2181

#�����ʼ�
mail.host=smtp.exmail.qq.com
mail.port=25
mail.username=admin@dtds.com.cn
mail.password=admin
	

