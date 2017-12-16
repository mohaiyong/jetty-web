package com.momo.scheduler;

import org.springframework.stereotype.Component;

import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.handler.IJobHandler;
import com.spring.scheduler.core.handler.annotation.JobHander;


/**
 * 任务Handler的一个Demo（Bean模式�?
 * 
 * �?��步骤�?
 * 1、新建一个继承com.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加�?@Component”注解；
 * 3、添�?“@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，该名称对应的是调度中心新建任务的JobHandler属�?的�?�?
 * 4、执行日志：�?��通过 "jobLogger.log" 打印执行日志�?
 * 
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHander(value="demoJobHandler")
@Component
public class JobHandlerDemo extends IJobHandler {

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		for (int i = 0; i < 5; i++) {
			System.err.println("JobHandlerDemo............任务调度成功");
		}
		return ReturnT.SUCCESS;
	}
	
}
