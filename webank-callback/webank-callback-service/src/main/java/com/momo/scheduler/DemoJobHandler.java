package com.momo.scheduler;

import org.springframework.stereotype.Component;

import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.handler.IJobHandler;
import com.spring.scheduler.core.handler.annotation.JobHander;


/**
 * 任务Handler的一个Demo（Bean模式）
 * 
 * 开发步骤：
 * 1、新建一个继承com.xxl.job.core.handler.IJobHandler的Java类；
 * 2、该类被Spring容器扫描为Bean实例，如加“@Component”注解；
 * 3、添加 “@JobHander(value="自定义jobhandler名称")”注解，注解的value值为自定义的JobHandler名称，该名称对应的是调度中心新建任务的JobHandler属性的值。
 * 4、执行日志：需要通过 "jobLogger.log" 打印执行日志；
 * 
 * @author xuxueli 2015-12-19 19:43:36
 */
@JobHander(value="jobHandlerDemo")
@Component
public class DemoJobHandler extends IJobHandler {

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		for (int i = 0; i < 5; i++) {
			System.err.println("demo任务调用成功");
		}
		ReturnT<String> rt = new ReturnT<String>(ReturnT.SUCCESS_CODE, "demo任务调用成功");
		return rt;
	}
	
}
