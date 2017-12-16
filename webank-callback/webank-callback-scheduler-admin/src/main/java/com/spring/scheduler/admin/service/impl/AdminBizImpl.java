package com.spring.scheduler.admin.service.impl;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.admin.core.model.JobLog;
import com.spring.scheduler.admin.core.schedule.JobDynamicScheduler;
import com.spring.scheduler.admin.core.util.AESUtil;
import com.spring.scheduler.admin.core.util.RandomUtil;
import com.spring.scheduler.admin.dao.JobInfoDao;
import com.spring.scheduler.admin.dao.JobLogDao;
import com.spring.scheduler.admin.dao.JobRegistryDao;
import com.spring.scheduler.core.biz.AdminBiz;
import com.spring.scheduler.core.biz.model.HandleCallbackParam;
import com.spring.scheduler.core.biz.model.RegistryParam;
import com.spring.scheduler.core.biz.model.ReturnT;

/**
 * ClassName: AdminBizImpl 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
@Service
public class AdminBizImpl implements AdminBiz {
    private static Logger logger = LoggerFactory.getLogger(AdminBizImpl.class);

    @Resource
    public JobLogDao jobLogDao;
    @Resource
    private JobInfoDao jobInfoDao;
    @Resource
    private JobRegistryDao jobRegistryDao;

    @Override
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList) {
        for (HandleCallbackParam handleCallbackParam: callbackParamList) {
            ReturnT<String> callbackResult = callback(handleCallbackParam);
            logger.info("JobApiController.callback {}, handleCallbackParam={}, callbackResult={}",
                    (callbackResult.getCode()==ReturnT.SUCCESS_CODE?"success":"fail"), handleCallbackParam, callbackResult);
        }

        return ReturnT.SUCCESS;
    }

    private ReturnT<String> callback(HandleCallbackParam handleCallbackParam) {
        // valid log item
        JobLog log = jobLogDao.load(handleCallbackParam.getLogId());
        if (log == null) {
            return new ReturnT<String>(ReturnT.FAIL_CODE, "log item not found.");
        }

        // trigger success, to trigger child job, and avoid repeat trigger child job
        String childTriggerMsg = null;
        if (ReturnT.SUCCESS_CODE==handleCallbackParam.getExecuteResult().getCode() && ReturnT.SUCCESS_CODE!=log.getHandleCode()) {
            JobInfo jobInfo = jobInfoDao.loadById(log.getJobId());
            if (jobInfo!=null && StringUtils.isNotBlank(jobInfo.getChildJobKey())) {
                childTriggerMsg = "<hr>";
                String[] childJobKeys = jobInfo.getChildJobKey().split(",");
                for (int i = 0; i < childJobKeys.length; i++) {
                    String[] jobKeyArr = childJobKeys[i].split("_");
                    if (jobKeyArr!=null && jobKeyArr.length==2) {
                        JobInfo childJobInfo = jobInfoDao.loadById(Integer.valueOf(jobKeyArr[1]));
                        if (childJobInfo!=null) {
                            try {
                                boolean ret = JobDynamicScheduler.triggerJob(String.valueOf(childJobInfo.getId()), String.valueOf(childJobInfo.getJobGroup()));

                                // add msg
                                childTriggerMsg += MessageFormat.format("<br> {0}/{1} 子任务Key:[{2}]已被执行, status: {3}, 任务描述: {4}",
                                        (i+1), childJobKeys.length, childJobKeys[i], ret, childJobInfo.getJobDesc());
                            } catch (SchedulerException e) {
                                logger.error("", e);
                            }
                        } else {
                            childTriggerMsg += MessageFormat.format("<br> {0}/{1} 子任务key:[{2}]不存在",
                                    (i+1), childJobKeys.length, childJobKeys[i]);
                        }
                    } else {
                        childTriggerMsg += MessageFormat.format("<br> {0}/{1} 子任务key:[{2}]格式不正确",
                                (i+1), childJobKeys.length, childJobKeys[i]);
                    }
                }

            }
        }

        // handle msg
        StringBuffer handleMsg = new StringBuffer();
        if (log.getHandleMsg()!=null) {
            handleMsg.append(log.getHandleMsg()).append("<br>");
        }
        if (handleCallbackParam.getExecuteResult().getMsg() != null) {
            handleMsg.append(handleCallbackParam.getExecuteResult().getMsg());
        }
        if (childTriggerMsg !=null) {
            handleMsg.append("<br>子任务执行日志：").append(childTriggerMsg);
        }

        // success, save log
        log.setHandleTime(new Date());
        log.setHandleCode(handleCallbackParam.getExecuteResult().getCode());
        log.setHandleMsg(handleMsg.toString());
        jobLogDao.updateHandleInfo(log);
        return ReturnT.SUCCESS;
    }

    @Override
    public ReturnT<String> registry(RegistryParam registryParam) {
    	ReturnT<String> rt= new ReturnT<String>();
    	rt.setCode(ReturnT.SUCCESS_CODE);
    	rt.setMsg("客户端注册成功");
        int ret = jobRegistryDao.registryUpdate(registryParam.getRegistGroup(), registryParam.getExecutorClient(), registryParam.getRegistryValue());
        if (ret < 1) {
            try {
				jobRegistryDao.registrySave(registryParam.getRegistGroup(), AESUtil.aesEncrypt(RandomUtil.getRandomNum(16), RandomUtil.getRandomNum(128)), registryParam.getRegistryValue(),registryParam.getExecutorClient(),registryParam.getClientName());
			} catch (Exception e) {
				rt.setCode(ReturnT.FAIL_CODE);
				rt.setMsg("客户端注册失败");
				logger.error("客户端注册失败：",e);
			}
        }
        return rt;
    }
}
