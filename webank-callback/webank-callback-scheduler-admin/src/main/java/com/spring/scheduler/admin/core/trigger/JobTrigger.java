package com.spring.scheduler.admin.core.trigger;

import java.util.ArrayList;
import java.util.Date;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.scheduler.admin.core.enums.ExecutorFailStrategyEnum;
import com.spring.scheduler.admin.core.model.JobGroup;
import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.admin.core.model.JobLog;
import com.spring.scheduler.admin.core.model.JobRegistry;
import com.spring.scheduler.admin.core.route.ExecutorRouteStrategyEnum;
import com.spring.scheduler.admin.core.schedule.JobDynamicScheduler;
import com.spring.scheduler.admin.core.thread.JobFailMonitorHelper;
import com.spring.scheduler.core.biz.ExecutorBiz;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.biz.model.TriggerParam;
import com.spring.scheduler.core.enums.ExecutorBlockStrategyEnum;
import com.spring.scheduler.core.util.AESUtil;

/**
 * ClassName: JobTrigger 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月21日
 */
public class JobTrigger {
    private static Logger logger = LoggerFactory.getLogger(JobTrigger.class);

    /**
     * @Description: 根据任务ID触发
     * @param jobId   
     * @return void  
     * @throws Exception 
     * @throws
     * @author JornTang
     * @email 957707261@qq.com
     * @date 2017年8月17日
     */
    public static void trigger(int jobId) throws Exception {

        //获取任务信息
        JobInfo jobInfo = JobDynamicScheduler.jobInfoDao.loadById(jobId);              // job info
        //获取分组信息
        JobGroup group = JobDynamicScheduler.jobGroupDao.load(jobInfo.getJobGroup());  // group info
        //获取注册信息
        JobRegistry registry = JobDynamicScheduler.jobRegistryDao.findById(group.getRegistId());  // group regist
        if(registry.getIfGrant()== 0){
        	logger.info("执行器【"+registry.getClientName()+"】未被授权,拒绝调度");
        	return;
        }
        //阻塞处理策略
        ExecutorBlockStrategyEnum blockStrategy = ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), ExecutorBlockStrategyEnum.SERIAL_EXECUTION);  // block strategy
        //失败处理策略
        ExecutorFailStrategyEnum failStrategy = ExecutorFailStrategyEnum.match(jobInfo.getExecutorFailStrategy(), ExecutorFailStrategyEnum.FAIL_ALARM);    // fail strategy
        //路由策略
        ExecutorRouteStrategyEnum executorRouteStrategyEnum = ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null);    // route strategy
        //地址集合
        ArrayList<String> addressList = (ArrayList<String>) group.getRegistryList();

        //分片路由处理
        if (ExecutorRouteStrategyEnum.SHARDING_BROADCAST == executorRouteStrategyEnum && CollectionUtils.isNotEmpty(addressList)) {
            for (int i = 0; i < addressList.size(); i++) {
                String address = addressList.get(i);

                //日志记录
                JobLog jobLog = new JobLog();
                jobLog.setJobGroup(jobInfo.getJobGroup());
                jobLog.setJobId(jobInfo.getId());
                JobDynamicScheduler.jobLogDao.save(jobLog);
                logger.debug(">>>>>>>>>>>  trigger start, jobId:{}", jobLog.getId());

                //日志记录
                jobLog.setGlueType(jobInfo.getGlueType());
                jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
                jobLog.setExecutorParam(jobInfo.getExecutorParam());
                jobLog.setTriggerTime(new Date());

                ReturnT<String> triggerResult = new ReturnT<String>(null);
                StringBuffer triggerMsgSb = new StringBuffer();
                triggerMsgSb.append("注册方式：").append( (group.getAddressType() == 0)?"自动注册":"手动录入" );
                triggerMsgSb.append("<br>阻塞处理策略：").append(blockStrategy.getTitle());
                triggerMsgSb.append("<br>失败处理策略：").append(failStrategy.getTitle());
                triggerMsgSb.append("<br>地址列表：").append(group.getRegistryList());
                triggerMsgSb.append("<br>路由策略：").append(executorRouteStrategyEnum.getTitle());
                
                //判断地址是否为空
                if (triggerResult.getCode()==ReturnT.SUCCESS_CODE && CollectionUtils.isEmpty(addressList)) {
                    triggerResult.setCode(ReturnT.FAIL_CODE);
                    triggerMsgSb.append("<br>----------------------<br>").append("调度失败：").append("执行器地址为空");
                }

                if (triggerResult.getCode() == ReturnT.SUCCESS_CODE) {
                	//构造触发参数
                    TriggerParam triggerParam = new TriggerParam();
                    triggerParam.setJobId(jobInfo.getId());
                    triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
                    triggerParam.setExecutorParams(jobInfo.getExecutorParam());
                    triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
                    triggerParam.setLogId(jobLog.getId());
                    triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
                    triggerParam.setGlueType(jobInfo.getGlueType());
                    triggerParam.setGlueSource(jobInfo.getGlueSource());
                    triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
                    triggerParam.setBroadcastIndex(i);
                    triggerParam.setBroadcastTotal(addressList.size()); // update02
                    triggerParam.setClientKey(registry.getRegistryKey());
                    triggerParam.setAccessToken(AESUtil.aesEncrypt(registry.getAccessToken() + "&" + System.currentTimeMillis(), registry.getAesKey()));

                    //触发调度
                    triggerResult = runExecutor(triggerParam, address);     // update03
                    triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());

                    //失败重试
                    if (triggerResult.getCode()!=ReturnT.SUCCESS_CODE && failStrategy == ExecutorFailStrategyEnum.FAIL_RETRY) {
                        triggerResult = runExecutor(triggerParam, address);  // update04
                        triggerMsgSb.append("<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>失败重试<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());
                    }
                }

                //日志记录
                jobLog.setExecutorAddress(triggerResult.getContent());
                jobLog.setTriggerCode(triggerResult.getCode());
                jobLog.setTriggerMsg(triggerMsgSb.toString());
                JobDynamicScheduler.jobLogDao.updateTriggerInfo(jobLog);

                //监控任务触发
                JobFailMonitorHelper.monitor(jobLog.getId());
                logger.debug(">>>>>>>>>>>  trigger end, jobId:{}", jobLog.getId());

            }
            return;
        }

        //构造日志信息
        JobLog jobLog = new JobLog();
        jobLog.setJobGroup(jobInfo.getJobGroup());
        jobLog.setJobId(jobInfo.getId());
        //保存日志
        JobDynamicScheduler.jobLogDao.save(jobLog);
        logger.debug(">>>>>>>>>>>  trigger start, jobId:{}", jobLog.getId());

        jobLog.setGlueType(jobInfo.getGlueType());
        jobLog.setExecutorHandler(jobInfo.getExecutorHandler());
        jobLog.setExecutorParam(jobInfo.getExecutorParam());
        jobLog.setTriggerTime(new Date());

        ReturnT<String> triggerResult = new ReturnT<String>(null);
        StringBuffer triggerMsgSb = new StringBuffer();
        triggerMsgSb.append("注册方式：").append( (group.getAddressType() == 0)?"自动注册":"手动录入" );
        triggerMsgSb.append("<br>阻塞处理策略：").append(blockStrategy.getTitle());
        triggerMsgSb.append("<br>失败处理策略：").append(failStrategy.getTitle());
        triggerMsgSb.append("<br>地址列表：").append(group.getRegistryList());
        triggerMsgSb.append("<br>路由策略：").append(executorRouteStrategyEnum.getTitle());
        
        //判断地址是否为空
        if (triggerResult.getCode()==ReturnT.SUCCESS_CODE && CollectionUtils.isEmpty(addressList)) {
            triggerResult.setCode(ReturnT.FAIL_CODE);
            triggerMsgSb.append("<br>----------------------<br>").append("调度失败：").append("执行器地址为空");
        }

        if (triggerResult.getCode() == ReturnT.SUCCESS_CODE) {
            //构造触发参数
            TriggerParam triggerParam = new TriggerParam();
            triggerParam.setJobId(jobInfo.getId());
            triggerParam.setExecutorHandler(jobInfo.getExecutorHandler());
            triggerParam.setExecutorParams(jobInfo.getExecutorParam());
            triggerParam.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
            triggerParam.setLogId(jobLog.getId());
            triggerParam.setLogDateTim(jobLog.getTriggerTime().getTime());
            triggerParam.setGlueType(jobInfo.getGlueType());
            triggerParam.setGlueSource(jobInfo.getGlueSource());
            triggerParam.setGlueUpdatetime(jobInfo.getGlueUpdatetime().getTime());
            triggerParam.setBroadcastIndex(0);
            triggerParam.setBroadcastTotal(1);
            triggerParam.setClientKey(registry.getRegistryKey());
            triggerParam.setAccessToken(AESUtil.aesEncrypt(registry.getAccessToken() + "&" + System.currentTimeMillis(), registry.getAesKey()));

            //触发调度
            triggerResult = executorRouteStrategyEnum.getRouter().routeRun(triggerParam, addressList);
            triggerMsgSb.append("<br><br><span style=\"color:#00c0ef;\" > >>>>>>>>>>>触发调度<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());

            //失败重试
            if (triggerResult.getCode()!=ReturnT.SUCCESS_CODE && failStrategy == ExecutorFailStrategyEnum.FAIL_RETRY) {
                triggerResult = executorRouteStrategyEnum.getRouter().routeRun(triggerParam, addressList);
                triggerMsgSb.append("<br><br><span style=\"color:#F39C12;\" > >>>>>>>>>>>失败重试<<<<<<<<<<< </span><br>").append(triggerResult.getMsg());
            }
        }

        //日志记录
        jobLog.setExecutorAddress(triggerResult.getContent());
        jobLog.setTriggerCode(triggerResult.getCode());
        jobLog.setTriggerMsg(triggerMsgSb.toString());
        JobDynamicScheduler.jobLogDao.updateTriggerInfo(jobLog);

        //监控任务触发
        JobFailMonitorHelper.monitor(jobLog.getId());
        logger.debug(">>>>>>>>>>>  trigger end, jobId:{}", jobLog.getId());
    }

    /**
     * @Description: 根据触发参数执行任务
     * @param triggerParam
     * @param address
     * @return   
     * @return ReturnT<String>  
     * @throws
     * @author JornTang
     * @email 957707261@qq.com
     * @date 2017年8月17日
     */
    public static ReturnT<String> runExecutor(TriggerParam triggerParam, String address){
        ReturnT<String> runResult = null;
        try {
        	//获取NetComClientProxy
            ExecutorBiz executorBiz = JobDynamicScheduler.getExecutorBiz(address);
            //执行代理
            runResult = executorBiz.run(triggerParam);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            runResult = new ReturnT<String>(ReturnT.FAIL_CODE, ""+e );
        }

        StringBuffer runResultSB = new StringBuffer("执行日志信息：");
        runResultSB.append("<br>address ").append(address);
        runResultSB.append("<br>code ").append(runResult.getCode());
        runResultSB.append("<br>msg ").append(runResult.getMsg());

        runResult.setMsg(runResultSB.toString());
        runResult.setContent(address);
        return runResult;
    }

}
