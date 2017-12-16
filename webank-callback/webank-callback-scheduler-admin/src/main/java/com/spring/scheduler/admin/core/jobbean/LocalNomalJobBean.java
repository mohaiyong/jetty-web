package com.spring.scheduler.admin.core.jobbean;
//package com.action.job;
//
//import java.io.PrintWriter;
//import java.io.StringWriter;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.quartz.JobExecutionContext;
//import org.quartz.JobExecutionException;
//import org.quartz.JobKey;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.scheduling.quartz.QuartzJobBean;
//
//import com.client.handler.HandlerRouter;
//import com.client.util.jobNetCommUtil.RemoteCallBack;
//import com.client.util.JacksonUtil;
//import com.core.model.jobInfo;
//import com.core.model.jobLog;
//import com.core.thread.JobFailMonitorHelper;
//import com.core.util.DynamicSchedulerUtil;
//
///**
// * http job bean
// * @author xuxueli 2015-12-17 18:20:34
// */
//@Deprecated
//public abstract class LocalNomalJobBean extends QuartzJobBean {
//	private static Logger logger = LoggerFactory.getLogger(LocalNomalJobBean.class);
//
//	@Override
//	protected void executeInternal(JobExecutionContext context)
//			throws JobExecutionException {
//		JobKey jobKey = context.getTrigger().getJobKey();
//		
//		jobInfo jobInfo = DynamicSchedulerUtil.jobInfoDao.load(jobKey.getGroup(), jobKey.getName());
//		@SuppressWarnings("unchecked")
//		HashMap<String, String> jobDataMap = (HashMap<String, String>) JacksonUtil.readValueRefer(jobInfo.getJobData(), Map.class);
//		
//		// save log
//		jobLog jobLog = new jobLog();
//		jobLog.setJobGroup(jobInfo.getJobGroup());
//		jobLog.setJobName(jobInfo.getJobName());
//		jobLog.setJobCron(jobInfo.getJobCron());
//		jobLog.setJobDesc(jobInfo.getJobDesc());
//		jobLog.setJobClass(jobInfo.getJobClass());
//		jobLog.setJobData(jobInfo.getJobData());
//		
//		jobLog.setJobClass(RemoteHttpJobBean.class.getName());
//		jobLog.setJobData(jobInfo.getJobData());
//		DynamicSchedulerUtil.jobLogDao.save(jobLog);
//		logger.info(">>>>>>>>>>>  trigger start, jobLog:{}", jobLog);
//		
//		// trigger request
//		String handler_params = jobDataMap.get(HandlerRouter.HANDLER_PARAMS);
//		String[] handlerParams = null;
//		if (StringUtils.isNotBlank(handler_params)) {
//			handlerParams = handler_params.split(",");
//		}
//		
//		jobLog.setTriggerTime(new Date());
//		jobLog.setTriggerStatus(RemoteCallBack.SUCCESS);
//		jobLog.setTriggerMsg(null);
//		
//		try {
//			Object responseMsg = this.handle(handlerParams);
//			
//			jobLog.setHandleTime(new Date());
//			jobLog.setHandleStatus(RemoteCallBack.SUCCESS);
//			jobLog.setHandleMsg(JacksonUtil.writeValueAsString(responseMsg));
//		} catch (Exception e) {
//			logger.info("JobThread Exception:", e);
//			StringWriter out = new StringWriter();
//			e.printStackTrace(new PrintWriter(out));
//			
//			jobLog.setHandleTime(new Date());
//			jobLog.setHandleStatus(RemoteCallBack.FAIL);
//			jobLog.setHandleMsg(out.toString());
//		}
//		
//		// update trigger info
//		DynamicSchedulerUtil.jobLogDao.updateTriggerInfo(jobLog);
//		DynamicSchedulerUtil.jobLogDao.updateHandleInfo(jobLog);
//		JobFailMonitorHelper.monitor(jobLog.getId());
//		logger.info(">>>>>>>>>>>  trigger end, jobLog.id:{}, jobLog:{}", jobLog.getId(), jobLog);
//		
//    }
//	
//	public abstract Object handle(String... param);
//	
//}