package com.spring.scheduler.admin.core.jobbean;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

import com.spring.scheduler.admin.core.trigger.JobTrigger;

/**
 * ClassName: RemoteHttpJobBean 
 * @Description: 
 * http job bean
 * “@DisallowConcurrentExecution” diable concurrent, thread size can not be only one, better given more
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
//@DisallowConcurrentExecution
public class RemoteHttpJobBean extends QuartzJobBean {
	private static Logger logger = LoggerFactory.getLogger(RemoteHttpJobBean.class);

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {

		// load jobId
		JobKey jobKey = context.getTrigger().getJobKey();
		Integer jobId = Integer.valueOf(jobKey.getName());

		// trigger
		try {
			JobTrigger.trigger(jobId);
		} catch (Exception e) {
			logger.error("job trgger error",e);
		}
	}

}