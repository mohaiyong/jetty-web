/*
SQLyog Ultimate v11.33 (64 bit)
MySQL - 5.6.20 : Database - ss_job
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`ss_job` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `ss_job`;

/*Table structure for table `job_qrtz_blob_triggers` */

DROP TABLE IF EXISTS `job_qrtz_blob_triggers`;

CREATE TABLE `job_qrtz_blob_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `BLOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `job_qrtz_blob_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `job_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_blob_triggers` */

/*Table structure for table `job_qrtz_calendars` */

DROP TABLE IF EXISTS `job_qrtz_calendars`;

CREATE TABLE `job_qrtz_calendars` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `CALENDAR_NAME` varchar(200) NOT NULL,
  `CALENDAR` blob NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`CALENDAR_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_calendars` */

/*Table structure for table `job_qrtz_cron_triggers` */

DROP TABLE IF EXISTS `job_qrtz_cron_triggers`;

CREATE TABLE `job_qrtz_cron_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `CRON_EXPRESSION` varchar(200) NOT NULL,
  `TIME_ZONE_ID` varchar(80) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `job_qrtz_cron_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `job_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_cron_triggers` */

insert  into `job_qrtz_cron_triggers`(`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`CRON_EXPRESSION`,`TIME_ZONE_ID`) values ('quartzScheduler','17394','1','58 54 14 * * ?','GMT+08:00'),('quartzScheduler','17396','1','0 5 18 * * ?','GMT+08:00'),('quartzScheduler','32','1','0/15 * * * * ?','GMT+08:00');

/*Table structure for table `job_qrtz_fired_triggers` */

DROP TABLE IF EXISTS `job_qrtz_fired_triggers`;

CREATE TABLE `job_qrtz_fired_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `ENTRY_ID` varchar(95) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `FIRED_TIME` bigint(13) NOT NULL,
  `SCHED_TIME` bigint(13) NOT NULL,
  `PRIORITY` int(11) NOT NULL,
  `STATE` varchar(16) NOT NULL,
  `JOB_NAME` varchar(200) DEFAULT NULL,
  `JOB_GROUP` varchar(200) DEFAULT NULL,
  `IS_NONCONCURRENT` varchar(1) DEFAULT NULL,
  `REQUESTS_RECOVERY` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`ENTRY_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_fired_triggers` */

/*Table structure for table `job_qrtz_job_details` */

DROP TABLE IF EXISTS `job_qrtz_job_details`;

CREATE TABLE `job_qrtz_job_details` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `JOB_CLASS_NAME` varchar(250) NOT NULL,
  `IS_DURABLE` varchar(1) NOT NULL,
  `IS_NONCONCURRENT` varchar(1) NOT NULL,
  `IS_UPDATE_DATA` varchar(1) NOT NULL,
  `REQUESTS_RECOVERY` varchar(1) NOT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_job_details` */

insert  into `job_qrtz_job_details`(`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`,`DESCRIPTION`,`JOB_CLASS_NAME`,`IS_DURABLE`,`IS_NONCONCURRENT`,`IS_UPDATE_DATA`,`REQUESTS_RECOVERY`,`JOB_DATA`) values ('quartzScheduler','17394','1',NULL,'com.spring.scheduler.admin.core.jobbean.RemoteHttpJobBean','0','0','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xp\0sr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0\0x\0'),('quartzScheduler','17396','1',NULL,'com.spring.scheduler.admin.core.jobbean.RemoteHttpJobBean','0','0','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xp\0sr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0\0x\0'),('quartzScheduler','32','1',NULL,'com.spring.scheduler.admin.core.jobbean.RemoteHttpJobBean','0','0','0','0','��\0sr\0org.quartz.JobDataMap���迩��\0\0xr\0&org.quartz.utils.StringKeyDirtyFlagMap�����](\0Z\0allowsTransientDataxr\0org.quartz.utils.DirtyFlagMap�.�(v\n�\0Z\0dirtyL\0mapt\0Ljava/util/Map;xp\0sr\0java.util.HashMap���`�\0F\0\nloadFactorI\0	thresholdxp?@\0\0\0\0\0w\0\0\0\0\0\0\0x\0');

/*Table structure for table `job_qrtz_locks` */

DROP TABLE IF EXISTS `job_qrtz_locks`;

CREATE TABLE `job_qrtz_locks` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `LOCK_NAME` varchar(40) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`LOCK_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_locks` */

insert  into `job_qrtz_locks`(`SCHED_NAME`,`LOCK_NAME`) values ('quartzScheduler','STATE_ACCESS'),('quartzScheduler','TRIGGER_ACCESS');

/*Table structure for table `job_qrtz_paused_trigger_grps` */

DROP TABLE IF EXISTS `job_qrtz_paused_trigger_grps`;

CREATE TABLE `job_qrtz_paused_trigger_grps` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_paused_trigger_grps` */

/*Table structure for table `job_qrtz_scheduler_state` */

DROP TABLE IF EXISTS `job_qrtz_scheduler_state`;

CREATE TABLE `job_qrtz_scheduler_state` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `INSTANCE_NAME` varchar(200) NOT NULL,
  `LAST_CHECKIN_TIME` bigint(13) NOT NULL,
  `CHECKIN_INTERVAL` bigint(13) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`INSTANCE_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_scheduler_state` */

insert  into `job_qrtz_scheduler_state`(`SCHED_NAME`,`INSTANCE_NAME`,`LAST_CHECKIN_TIME`,`CHECKIN_INTERVAL`) values ('quartzScheduler','MS-20170520VDBL1509075609635',1509102136806,5000);

/*Table structure for table `job_qrtz_simple_triggers` */

DROP TABLE IF EXISTS `job_qrtz_simple_triggers`;

CREATE TABLE `job_qrtz_simple_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `REPEAT_COUNT` bigint(7) NOT NULL,
  `REPEAT_INTERVAL` bigint(12) NOT NULL,
  `TIMES_TRIGGERED` bigint(10) NOT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `job_qrtz_simple_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `job_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_simple_triggers` */

/*Table structure for table `job_qrtz_simprop_triggers` */

DROP TABLE IF EXISTS `job_qrtz_simprop_triggers`;

CREATE TABLE `job_qrtz_simprop_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `STR_PROP_1` varchar(512) DEFAULT NULL,
  `STR_PROP_2` varchar(512) DEFAULT NULL,
  `STR_PROP_3` varchar(512) DEFAULT NULL,
  `INT_PROP_1` int(11) DEFAULT NULL,
  `INT_PROP_2` int(11) DEFAULT NULL,
  `LONG_PROP_1` bigint(20) DEFAULT NULL,
  `LONG_PROP_2` bigint(20) DEFAULT NULL,
  `DEC_PROP_1` decimal(13,4) DEFAULT NULL,
  `DEC_PROP_2` decimal(13,4) DEFAULT NULL,
  `BOOL_PROP_1` varchar(1) DEFAULT NULL,
  `BOOL_PROP_2` varchar(1) DEFAULT NULL,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  CONSTRAINT `job_qrtz_simprop_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`) REFERENCES `job_qrtz_triggers` (`SCHED_NAME`, `TRIGGER_NAME`, `TRIGGER_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_simprop_triggers` */

/*Table structure for table `job_qrtz_trigger_group` */

DROP TABLE IF EXISTS `job_qrtz_trigger_group`;

CREATE TABLE `job_qrtz_trigger_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `app_name` varchar(64) NOT NULL COMMENT '执行器AppName',
  `title` varchar(12) NOT NULL COMMENT '执行器名称',
  `order` tinyint(4) NOT NULL DEFAULT '0' COMMENT '排序',
  `address_type` tinyint(4) NOT NULL DEFAULT '0' COMMENT '执行器地址类型：0=自动注册、1=手动录入',
  `address_list` varchar(200) DEFAULT NULL COMMENT '执行器地址列表，多地址逗号分隔',
  `regist_id` int(16) DEFAULT '-1' COMMENT '客户端注册ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_trigger_group` */

insert  into `job_qrtz_trigger_group`(`id`,`app_name`,`title`,`order`,`address_type`,`address_list`,`regist_id`) values (1,'archive-inner','中山文档平台内网',1,1,'127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action',7);

/*Table structure for table `job_qrtz_trigger_info` */

DROP TABLE IF EXISTS `job_qrtz_trigger_info`;

CREATE TABLE `job_qrtz_trigger_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_cron` varchar(128) NOT NULL COMMENT '任务执行CRON',
  `job_desc` varchar(255) NOT NULL,
  `add_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `author` varchar(64) DEFAULT NULL COMMENT '作者',
  `alarm_email` varchar(255) DEFAULT NULL COMMENT '报警邮件',
  `executor_route_strategy` varchar(50) DEFAULT NULL COMMENT '执行器路由策略',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(255) DEFAULT NULL COMMENT '执行器任务参数',
  `executor_block_strategy` varchar(50) DEFAULT NULL COMMENT '阻塞处理策略',
  `executor_fail_strategy` varchar(50) DEFAULT NULL COMMENT '失败处理策略',
  `glue_type` varchar(50) NOT NULL COMMENT 'GLUE类型',
  `glue_source` text COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) DEFAULT NULL COMMENT 'GLUE备注',
  `glue_updatetime` datetime DEFAULT NULL COMMENT 'GLUE更新时间',
  `child_jobkey` varchar(255) DEFAULT NULL COMMENT '子任务Key',
  `control_flag` int(1) NOT NULL DEFAULT '0' COMMENT '操控标志0=调度中心控制，1=外系统控制',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=17397 DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_trigger_info` */

insert  into `job_qrtz_trigger_info`(`id`,`job_group`,`job_cron`,`job_desc`,`add_time`,`update_time`,`author`,`alarm_email`,`executor_route_strategy`,`executor_handler`,`executor_param`,`executor_block_strategy`,`executor_fail_strategy`,`glue_type`,`glue_source`,`glue_remark`,`glue_updatetime`,`child_jobkey`,`control_flag`) values (17396,1,'0 5 18 * * ?','测试任务','2017-09-25 11:39:39','2017-10-27 10:29:57','文档管理员','','FIRST','jobHandler','{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}','SERIAL_EXECUTION','FAIL_ALARM','BEAN',NULL,'GLUE代码初始化','2017-09-25 11:39:39','',1);

/*Table structure for table `job_qrtz_trigger_log` */

DROP TABLE IF EXISTS `job_qrtz_trigger_log`;

CREATE TABLE `job_qrtz_trigger_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_group` int(11) NOT NULL COMMENT '执行器主键ID',
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `executor_address` varchar(255) DEFAULT NULL COMMENT '执行器地址，本次执行的地址',
  `executor_handler` varchar(255) DEFAULT NULL COMMENT '执行器任务handler',
  `executor_param` varchar(255) DEFAULT NULL COMMENT 'executor_param',
  `trigger_time` datetime DEFAULT NULL COMMENT '调度-时间',
  `trigger_code` varchar(255) NOT NULL DEFAULT '0' COMMENT '调度-结果',
  `trigger_msg` varchar(2048) DEFAULT NULL COMMENT '调度-日志',
  `handle_time` datetime DEFAULT NULL COMMENT '执行-时间',
  `handle_code` varchar(255) NOT NULL DEFAULT '0' COMMENT '执行-状态',
  `handle_msg` varchar(2048) DEFAULT NULL COMMENT '执行-日志',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11796 DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_trigger_log` */

insert  into `job_qrtz_trigger_log`(`id`,`job_group`,`job_id`,`glue_type`,`executor_address`,`executor_handler`,`executor_param`,`trigger_time`,`trigger_code`,`trigger_msg`,`handle_time`,`handle_code`,`handle_msg`) values (11789,1,17396,'BEAN','127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action','jobHandler','{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}','2017-10-27 11:00:46','200','注册方式：手动录入<br>阻塞处理策略：单机串行<br>失败处理策略：失败告警<br>地址列表：[127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action]<br>路由策略：第一个<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>执行日志信息：<br>address 127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action<br>code 200<br>msg null',NULL,'0',NULL),(11790,1,17396,'BEAN','127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action','jobHandler','{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}','2017-10-27 11:06:17','500','注册方式：手动录入<br>阻塞处理策略：单机串行<br>失败处理策略：失败告警<br>地址列表：[127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action]<br>路由策略：第一个<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>执行日志信息：<br>address 127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action<br>code 500<br>msg java.lang.RuntimeException: Client-error:unknown code for readObject at 0x3c (<)',NULL,'0',NULL),(11791,1,17396,'BEAN','127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action','jobHandler','{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}','2017-10-27 11:06:31','500','注册方式：手动录入<br>阻塞处理策略：单机串行<br>失败处理策略：失败告警<br>地址列表：[127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action]<br>路由策略：第一个<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>执行日志信息：<br>address 127.0.0.1:7001/ArchiveWeb/intelligentAction!test.action<br>code 500<br>msg java.lang.RuntimeException: Client-error:unknown code for readObject at 0x3c (<)',NULL,'0',NULL),(11792,1,17396,'BEAN','127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action','jobHandler','{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}','2017-10-27 11:06:52','200','注册方式：手动录入<br>阻塞处理策略：单机串行<br>失败处理策略：失败告警<br>地址列表：[127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action]<br>路由策略：第一个<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>执行日志信息：<br>address 127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action<br>code 200<br>msg null',NULL,'0',NULL),(11793,1,17396,'BEAN','127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action','jobHandler','{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}','2017-10-27 11:15:21','200','注册方式：手动录入<br>阻塞处理策略：单机串行<br>失败处理策略：失败告警<br>地址列表：[127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action]<br>路由策略：第一个<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>执行日志信息：<br>address 127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action<br>code 200<br>msg 任务调用成功,执行时间【2017-10-27 11:15:35】。执行参数【{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}】',NULL,'0',NULL),(11794,1,17396,'BEAN','127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action','jobHandler','{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}','2017-10-27 11:40:31','200','注册方式：手动录入<br>阻塞处理策略：单机串行<br>失败处理策略：失败告警<br>地址列表：[127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action]<br>路由策略：第一个<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>执行日志信息：<br>address 127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action<br>code 200<br>msg 任务调用成功,执行时间【2017-10-27 11:40:39】。执行参数【{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}】',NULL,'0',NULL),(11795,1,17396,'BEAN','127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action','jobHandler','{\"archiveId\":1,\"roomId\":3,\"cateCode\":\"A1\"}','2017-10-27 18:05:00','500','注册方式：手动录入<br>阻塞处理策略：单机串行<br>失败处理策略：失败告警<br>地址列表：[127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action]<br>路由策略：第一个<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>执行日志信息：<br>address 127.0.0.1:7001/ArchiveWeb/intelligentAction!jobHandle.action<br>code 500<br>msg java.lang.RuntimeException: Client-error:Connect to 127.0.0.1:7001 [/127.0.0.1] failed: Connection refused: connect',NULL,'0',NULL);

/*Table structure for table `job_qrtz_trigger_logglue` */

DROP TABLE IF EXISTS `job_qrtz_trigger_logglue`;

CREATE TABLE `job_qrtz_trigger_logglue` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `job_id` int(11) NOT NULL COMMENT '任务，主键ID',
  `glue_type` varchar(50) DEFAULT NULL COMMENT 'GLUE类型',
  `glue_source` text COMMENT 'GLUE源代码',
  `glue_remark` varchar(128) NOT NULL COMMENT 'GLUE备注',
  `add_time` timestamp NULL DEFAULT NULL,
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_trigger_logglue` */

/*Table structure for table `job_qrtz_trigger_registry` */

DROP TABLE IF EXISTS `job_qrtz_trigger_registry`;

CREATE TABLE `job_qrtz_trigger_registry` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `registry_group` varchar(255) NOT NULL COMMENT '注册分组',
  `registry_key` varchar(255) NOT NULL COMMENT '注册KEY',
  `registry_value` varchar(255) NOT NULL COMMENT '注册值（IP地址、端口）',
  `executor_client` varchar(50) DEFAULT NULL COMMENT '授权客户端',
  `client_name` varchar(50) DEFAULT NULL COMMENT '客户端名称',
  `if_grant` int(1) DEFAULT '0' COMMENT '是否授权0=未授权，1=已授权，2=授权失败，3=取消授权',
  `aes_key` varchar(200) DEFAULT NULL COMMENT 'AES密钥',
  `access_token` varchar(200) DEFAULT NULL COMMENT '授权令牌',
  `crt_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_trigger_registry` */

insert  into `job_qrtz_trigger_registry`(`id`,`registry_group`,`registry_key`,`registry_value`,`executor_client`,`client_name`,`if_grant`,`aes_key`,`access_token`,`crt_time`,`update_time`) values (7,'EXECUTOR','iAx64tCH+9PKt3XsyHBDW5z7a6p3QxXxpGo3ruB5m/0=','192.168.8.17:9999','ARCHIVE-INNER','中山文档平台内网',1,'284a4e97f7aea10c2d88c850429dbec5','ORYBvA0OZSQYzj2oUs5a7ze37eOASKxejU+hme/ZVc3Oh5T6GRHtX0VZEblzIvDDnu7PLGlRY9ZL\r\nLSjOxX4rHg==','2017-08-30 11:44:45','2017-09-27 14:28:22'),(8,'EXECUTOR','Kpwf3F/8bw9OKVb0ExAzqXEmXTVDy8SqKU3Is/DaK88=','10.129.33.191:7001','ARCHIVE-INNER','中山文档平台内网',0,NULL,NULL,NULL,'2017-10-26 19:31:53'),(9,'EXECUTOR','/YUiKvxZHOJSy72UHmqrxkpcZYXoURBcPjQJz7XYRSI=','10.129.33.191:7002','ARCHIVE-INNER','中山文档平台内网',1,'3a6b1700e134700a8f2fc54818fd7de0','CTlzP8sr4ex7hii9cfQSoV34n1aemeHSVS6B6G79KJfBVFV1EXy6dU33wqiWQL/W9cr0R2m5gl3h\r\nSHNOTxAYdw==',NULL,'2017-10-27 11:15:35'),(10,'EXECUTOR','M9Qj48RBxLBOlhprCqMMCo1Drk9ToN2KywTm30t4+Bc=','10.129.33.191:9999','ARCHIVE-INNER','中山文档平台内网',0,NULL,NULL,NULL,'2017-10-27 11:15:43');

/*Table structure for table `job_qrtz_triggers` */

DROP TABLE IF EXISTS `job_qrtz_triggers`;

CREATE TABLE `job_qrtz_triggers` (
  `SCHED_NAME` varchar(120) NOT NULL,
  `TRIGGER_NAME` varchar(200) NOT NULL,
  `TRIGGER_GROUP` varchar(200) NOT NULL,
  `JOB_NAME` varchar(200) NOT NULL,
  `JOB_GROUP` varchar(200) NOT NULL,
  `DESCRIPTION` varchar(250) DEFAULT NULL,
  `NEXT_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PREV_FIRE_TIME` bigint(13) DEFAULT NULL,
  `PRIORITY` int(11) DEFAULT NULL,
  `TRIGGER_STATE` varchar(16) NOT NULL,
  `TRIGGER_TYPE` varchar(8) NOT NULL,
  `START_TIME` bigint(13) NOT NULL,
  `END_TIME` bigint(13) DEFAULT NULL,
  `CALENDAR_NAME` varchar(200) DEFAULT NULL,
  `MISFIRE_INSTR` smallint(2) DEFAULT NULL,
  `JOB_DATA` blob,
  PRIMARY KEY (`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`),
  KEY `SCHED_NAME` (`SCHED_NAME`,`JOB_NAME`,`JOB_GROUP`),
  CONSTRAINT `job_qrtz_triggers_ibfk_1` FOREIGN KEY (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`) REFERENCES `job_qrtz_job_details` (`SCHED_NAME`, `JOB_NAME`, `JOB_GROUP`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `job_qrtz_triggers` */

insert  into `job_qrtz_triggers`(`SCHED_NAME`,`TRIGGER_NAME`,`TRIGGER_GROUP`,`JOB_NAME`,`JOB_GROUP`,`DESCRIPTION`,`NEXT_FIRE_TIME`,`PREV_FIRE_TIME`,`PRIORITY`,`TRIGGER_STATE`,`TRIGGER_TYPE`,`START_TIME`,`END_TIME`,`CALENDAR_NAME`,`MISFIRE_INSTR`,`JOB_DATA`) values ('quartzScheduler','17394','1','17394','1',NULL,1505372098000,-1,5,'PAUSED','CRON',1503903404000,0,NULL,2,''),('quartzScheduler','17396','1','17396','1',NULL,1509185100000,1509098700000,5,'WAITING','CRON',1506310779000,0,NULL,2,''),('quartzScheduler','32','1','32','1',NULL,1504082970000,1504082955000,5,'PAUSED','CRON',1504055977000,0,NULL,2,'');

/*Table structure for table `xxl_mq_message` */

DROP TABLE IF EXISTS `xxl_mq_message`;

CREATE TABLE `xxl_mq_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL COMMENT '消息主题',
  `data` text NOT NULL COMMENT '消息数据, Map<String, String>对象系列化的JSON字符串',
  `delay_time` datetime NOT NULL COMMENT '延迟执行的时间, new Date()立即执行, 否则在延迟时间点之后开始执行;',
  `add_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `status` varchar(255) NOT NULL COMMENT '消息状态: NEW=新消息、ING=消费中、SUCCESS=消费成功、FAIL=消费失败、TIMEOUT=超时',
  `msg` text COMMENT '历史流转日志',
  `retry_count` int(11) NOT NULL DEFAULT '0' COMMENT '重试次数',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*Data for the table `xxl_mq_message` */

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
