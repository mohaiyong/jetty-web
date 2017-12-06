/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50528
Source Host           : localhost:3306
Source Database       : webank-callback

Target Server Type    : MYSQL
Target Server Version : 50528
File Encoding         : 65001

Date: 2017-11-24 14:32:09
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_user_info
-- ----------------------------
DROP TABLE IF EXISTS `t_user_info`;
CREATE TABLE `t_user_info` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT COMMENT '物理主键',
  `userid` varchar(32) DEFAULT NULL COMMENT '用户id',
  `username` varchar(32) DEFAULT NULL COMMENT '用户姓名',
  `password` varchar(126) DEFAULT NULL COMMENT '用户密码',
  `salt` varchar(32) DEFAULT NULL COMMENT '密码秘钥',
  `trynum` int(2) DEFAULT '0' COMMENT '登录次数',
  `email` varchar(32) DEFAULT NULL COMMENT '用户邮箱',
  `mobile` varchar(20) DEFAULT NULL COMMENT '用户电话',
  `address` varchar(100) DEFAULT NULL COMMENT '详细地址',
  `state` char(2) DEFAULT NULL COMMENT '用户状态：\r\n            0A 正常\r\n            0H 过期\r\n            0L 锁定',
  `createtime` timestamp NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `updatetime` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COMMENT='003用户信息表';

-- ----------------------------
-- Records of t_user_info
-- ----------------------------
INSERT INTO `t_user_info` VALUES ('1', '2015042000000900001', '一休哥', 'ce71ecde618da9193ca58333e01702e7998023a448966b0b1a03b7fdc37dae23', '461465', '3', 'admin01@mucfc.com', '15999536828', '深圳市南山区', '0A', null, null);
INSERT INTO `t_user_info` VALUES ('2', '2015042000000900002', '一休妹', 'ce71ecde618da9193ca58333e01702e7998023a448966b0b1a03b7fdc37dae23', '461465', '3', 'admin02@mucfc.com', '15999536829', '深圳市南山区', '0A', '2015-07-23 17:16:40', null);
