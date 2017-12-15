/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : diamond

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2017-12-07 18:27:32
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for config_info
-- ----------------------------
DROP TABLE IF EXISTS `config_info`;
CREATE TABLE `config_info` (
  `id` bigint(64) unsigned NOT NULL AUTO_INCREMENT,
  `data_id` varchar(255) NOT NULL DEFAULT '',
  `group_id` varchar(128) NOT NULL DEFAULT '',
  `content` longtext NOT NULL,
  `md5` varchar(32) NOT NULL DEFAULT '',
  `src_ip` varchar(20) DEFAULT NULL,
  `src_user` varchar(20) DEFAULT NULL,
  `gmt_create` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  `gmt_modified` datetime NOT NULL DEFAULT '2010-05-05 00:00:00',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_config_datagroup` (`data_id`,`group_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=214 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of config_info
-- ----------------------------
INSERT INTO `config_info` VALUES ('2', 'zookeeper', 'common', 'zookeeper=zookeeper://172.16.1.190:2181?backup172.16.1.191:2181,172.16.1.192:2181,172.16.1.193:2181\ntimeout=50000\nisOpenDubboTest=y\n', '6d70ad6fa16283b8dc007e3181215a02', '', '', '2016-04-26 10:43:00', '2017-01-18 15:33:24');
INSERT INTO `config_info` VALUES ('212', 'common', 'webank_callback', '#Dubbo配置\ndubbo.app.name=webank-callback-service\ndubbo.app.port=60880\ndubbo.app.address=zookeeper://172.16.1.120:2181?backup=172.16.1.121:2181,172.16.1.122:2181,172.16.1.123:2181\n\n#发送邮件\nmail.host=smtp.exmail.qq.com\nmail.port=25\nmail.username=admin@dtds.com.cn\nmail.password=admin', '4b04bc4395cf5f785fa75249a4b52f50', null, null, '2017-12-07 17:23:21', '2017-12-07 17:23:21');
INSERT INTO `config_info` VALUES ('213', 'mysql', 'webank_callback', '#数据库配置\ndataSource.driverClassName=com.mysql.jdbc.Driver\ndataSource.url=jdbc:mysql://localhost:3306/webank-callback\ndataSource.username = root\ndataSource.password = 123456\ndataSource.initialSize  =  2\ndataSource.maxActive = 30\ndataSource.maxIdle = 2\ndataSource.minIdle = 2\ndataSource.maxOpenPreparedStatements = 150\ndataSource.validationQuery = SELECT 1 FROM DUAL\ndataSource.testWhileIdle = true\ndataSource.testOnBorrow = false\ndataSource.testOnReturn = false\ndataSource.timeBetweenEvictionRunsMillis = 60000\ndataSource.minEvictableIdleTimeMillis = 300000\ndataSource.poolPreparedStatements = true\ndataSource.maxPoolPreparedStatementPerConnectionSize = 20\ndataSource.filters = stat', '31f2dacc1dfee13863eff73af61c7387', null, null, '2017-12-07 17:23:38', '2017-12-07 17:23:38');

-- ----------------------------
-- Table structure for sys_project
-- ----------------------------
DROP TABLE IF EXISTS `sys_project`;
CREATE TABLE `sys_project` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `name` varchar(255) DEFAULT NULL COMMENT '系统名称',
  `code` varchar(100) DEFAULT NULL COMMENT '系统编码',
  `domain` varchar(255) DEFAULT NULL COMMENT '域名',
  `status` int(11) DEFAULT '1' COMMENT '状态：1正常，0关闭',
  `type` int(11) DEFAULT NULL COMMENT '系统类型：0平台（全国），1县域',
  `user_id` int(11) DEFAULT NULL COMMENT '创建人',
  `datetime` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_project
-- ----------------------------
INSERT INTO `sys_project` VALUES ('8', '示例系统', 'demo', 'www.demo.com', '0', '0', '-1', '2016-04-19 16:41:39');
INSERT INTO `sys_project` VALUES ('9', '商品', 'goods', 'www.dsf.com', '0', '0', '-1', '2016-04-21 10:40:24');

-- ----------------------------
-- Table structure for sys_resources
-- ----------------------------
DROP TABLE IF EXISTS `sys_resources`;
CREATE TABLE `sys_resources` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(32) DEFAULT NULL,
  `prj_id` int(11) DEFAULT NULL COMMENT '系统id关联sys_project主键id',
  `parent_id` int(11) DEFAULT '-1' COMMENT '上级id',
  `parent_uid` varchar(32) DEFAULT NULL COMMENT '父级编码，取自动态原配置中的值',
  `type` varchar(20) DEFAULT NULL COMMENT '类型：menu，url',
  `code` varchar(100) DEFAULT NULL COMMENT '编码',
  `name` varchar(255) DEFAULT NULL COMMENT '名称',
  `value` varchar(225) DEFAULT NULL COMMENT '值',
  `level` int(11) DEFAULT NULL,
  `status` int(11) DEFAULT '1' COMMENT '状态：1开启，0关闭',
  `user_id` int(11) DEFAULT NULL,
  `datetime` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=67 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_resources
-- ----------------------------
INSERT INTO `sys_resources` VALUES ('63', '7c39df0848a948d3bd0ada87dbe1d07d', '8', '-1', 'demo', 'menu', 'demo-user-manager', '用户管理', '/user/manager', '1', '1', '-1', '2016-04-19 16:42:50');
INSERT INTO `sys_resources` VALUES ('64', '8ae4a3fad06144bb98bc984ca4bdf22f', '8', '63', '7c39df0848a948d3bd0ada87dbe1d07d', 'menu', 'demo-user-list', '用户列表', '/user/list', '2', '1', '-1', '2016-04-19 16:45:02');
INSERT INTO `sys_resources` VALUES ('65', '8df0db02b75444baa1131899a8c2f3b6', '8', '64', '8ae4a3fad06144bb98bc984ca4bdf22f', 'url', 'demo-user-btn-add', '添加用户', '/user/add', '3', '1', '-1', '2016-04-19 16:47:13');
INSERT INTO `sys_resources` VALUES ('66', '93ee47f4f13640f9a322406311411db9', '8', '64', '8ae4a3fad06144bb98bc984ca4bdf22f', 'url', 'demo-user-link-views', '查看', '/user/views', '3', '1', '-1', '2016-04-19 16:49:25');
