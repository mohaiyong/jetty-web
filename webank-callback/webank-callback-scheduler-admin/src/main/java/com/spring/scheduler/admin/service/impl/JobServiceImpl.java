package com.spring.scheduler.admin.service.impl;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.lang.time.FastDateFormat;
import org.eclipse.jetty.util.log.Log;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.spring.scheduler.admin.core.enums.ExecutorFailStrategyEnum;
import com.spring.scheduler.admin.core.model.JobGroup;
import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.admin.core.model.JobRegistry;
import com.spring.scheduler.admin.core.route.ExecutorRouteStrategyEnum;
import com.spring.scheduler.admin.core.schedule.JobDynamicScheduler;
import com.spring.scheduler.admin.core.util.AESUtil;
import com.spring.scheduler.admin.core.util.RandomUtil;
import com.spring.scheduler.admin.dao.JobGroupDao;
import com.spring.scheduler.admin.dao.JobInfoDao;
import com.spring.scheduler.admin.dao.JobLogDao;
import com.spring.scheduler.admin.dao.JobLogGlueDao;
import com.spring.scheduler.admin.dao.JobRegistryDao;
import com.spring.scheduler.admin.service.JobService;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.enums.ExecutorBlockStrategyEnum;
import com.spring.scheduler.core.glue.GlueTypeEnum;

/**
 * ClassName: JobServiceImpl 
 * @Description: core job action for 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
@Service
public class JobServiceImpl implements JobService {
	private static Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

	@Resource
	private JobGroupDao jobGroupDao;
	@Resource
	private JobInfoDao jobInfoDao;
	@Resource
	public JobLogDao jobLogDao;
	@Resource
	private JobLogGlueDao jobLogGlueDao;
	@Resource
	private JobRegistryDao jobRegistryDao;
	
	@Override
	public Map<String, Object> pageList(int start, int length, int jobGroup, String executorHandler, String filterTime) {

		// page list
		List<JobInfo> list = jobInfoDao.pageList(start, length, jobGroup, executorHandler);
		int list_count = jobInfoDao.pageListCount(start, length, jobGroup, executorHandler);
		
		// fill job info
		if (list!=null && list.size()>0) {
			for (JobInfo jobInfo : list) {
				JobDynamicScheduler.fillJobInfo(jobInfo);
			}
		}
		
		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
	    maps.put("recordsTotal", list_count);		// 总记录数
	    maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
	    maps.put("data", list);  					// 分页列表
		return maps;
	}

	@Override
	public ReturnT<String> add(JobInfo jobInfo) {
		// valid
		JobGroup group = jobGroupDao.load(jobInfo.getJobGroup());
		if (group == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请选择执行器");
		}
		if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入格式正确的Crons");
		}
		if (StringUtils.isBlank(jobInfo.getJobDesc())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入任务描述");
		}
		if (StringUtils.isBlank(jobInfo.getAuthor())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入负责人");
		}
		if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "路由策略非法");
		}
		if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "阻塞处理策略非法");
		}
		if (ExecutorFailStrategyEnum.match(jobInfo.getExecutorFailStrategy(), null) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "失败处理策略非法");
		}
		if (GlueTypeEnum.match(jobInfo.getGlueType()) == null) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "运行模式非法");
		}
		if (GlueTypeEnum.BEAN==GlueTypeEnum.match(jobInfo.getGlueType()) && StringUtils.isBlank(jobInfo.getExecutorHandler())) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入JobHandler");
		}

		// fix "\r" in shell
		if (GlueTypeEnum.GLUE_SHELL==GlueTypeEnum.match(jobInfo.getGlueType()) && jobInfo.getGlueSource()!=null) {
			jobInfo.setGlueSource(jobInfo.getGlueSource().replaceAll("\r", ""));
		}

		// childJobKey valid
		if (StringUtils.isNotBlank(jobInfo.getChildJobKey())) {
			String[] childJobKeys = jobInfo.getChildJobKey().split(",");
			for (String childJobKeyItem: childJobKeys) {
				String[] childJobKeyArr = childJobKeyItem.split("_");
				if (childJobKeyArr.length!=2) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("子任务Key({0})格式错误", childJobKeyItem));
				}
				JobInfo childJobInfo = jobInfoDao.loadById(Integer.valueOf(childJobKeyArr[1]));
				if (childJobInfo==null) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("子任务Key({0})无效", childJobKeyItem));
				}
			}
		}

		// add in db
		jobInfoDao.save(jobInfo);
		if (jobInfo.getId() < 1) {
			return new ReturnT<String>(ReturnT.FAIL_CODE, "新增任务失败");
		}
		// add in quartz
        String qz_group = String.valueOf(jobInfo.getJobGroup());
        String qz_name = String.valueOf(jobInfo.getId());
        try {
        	//动态添加任务，并启动任务
            JobDynamicScheduler.addJob(qz_name, qz_group, jobInfo.getJobCron());
            //如果是外系统控制添加任务时先暂停任务
            if(jobInfo.getControlFlag()== 1){
            	pause(jobInfo.getId());
            }
            //return Primary key
            Map<String,Object> extend= new HashMap<String,Object>();
            extend.put("jobId", jobInfo.getId());
            return new ReturnT(ReturnT.SUCCESS_CODE, null, extend);
        } catch (Exception e) {
            logger.error("", e);
            try {
                jobInfoDao.delete(jobInfo.getId());
                JobDynamicScheduler.removeJob(qz_name, qz_group);
            } catch (SchedulerException e1) {
                logger.error("", e1);
            }
            return new ReturnT<String>(ReturnT.FAIL_CODE, "新增任务失败:" + e.getMessage());
        }
	}
	@Override
	public ReturnT<String> reschedule(JobInfo jobInfo) {

		// valid
				if (!CronExpression.isValidExpression(jobInfo.getJobCron())) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入格式正确的“Cron”");
				}
				if (StringUtils.isBlank(jobInfo.getJobDesc())) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“任务描述”");
				}
				if (StringUtils.isBlank(jobInfo.getAuthor())) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, "请输入“负责人”");
				}
				if (ExecutorRouteStrategyEnum.match(jobInfo.getExecutorRouteStrategy(), null) == null) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, "路由策略非法");
				}
				if (ExecutorBlockStrategyEnum.match(jobInfo.getExecutorBlockStrategy(), null) == null) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, "阻塞处理策略非法");
				}
				if (ExecutorFailStrategyEnum.match(jobInfo.getExecutorFailStrategy(), null) == null) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, "失败处理策略非法");
				}

				// childJobKey valid
				if (StringUtils.isNotBlank(jobInfo.getChildJobKey())) {
					String[] childJobKeys = jobInfo.getChildJobKey().split(",");
					for (String childJobKeyItem: childJobKeys) {
						String[] childJobKeyArr = childJobKeyItem.split("_");
						if (childJobKeyArr.length!=2) {
							return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("子任务Key({0})格式错误", childJobKeyItem));
						}
		                JobInfo childJobInfo = jobInfoDao.loadById(Integer.valueOf(childJobKeyArr[1]));
						if (childJobInfo==null) {
							return new ReturnT<String>(ReturnT.FAIL_CODE, MessageFormat.format("子任务Key({0})无效", childJobKeyItem));
						}
					}
				}

				// stage job info
				JobInfo exists_jobInfo = jobInfoDao.loadById(jobInfo.getId());
				if (exists_jobInfo == null) {
					return new ReturnT<String>(ReturnT.FAIL_CODE, "参数异常");
				}
				//String old_cron = exists_jobInfo.getJobCron();

				exists_jobInfo.setJobCron(jobInfo.getJobCron());
				exists_jobInfo.setJobDesc(jobInfo.getJobDesc());
				exists_jobInfo.setAuthor(jobInfo.getAuthor());
				exists_jobInfo.setAlarmEmail(jobInfo.getAlarmEmail());
				exists_jobInfo.setExecutorRouteStrategy(jobInfo.getExecutorRouteStrategy());
				exists_jobInfo.setExecutorHandler(jobInfo.getExecutorHandler());
				if(exists_jobInfo.getControlFlag() == 1){
					exists_jobInfo.setExecutorParam(exists_jobInfo.getExecutorParam());
				}else{
					exists_jobInfo.setExecutorParam(jobInfo.getExecutorParam());
				}
				exists_jobInfo.setExecutorBlockStrategy(jobInfo.getExecutorBlockStrategy());
				exists_jobInfo.setExecutorFailStrategy(jobInfo.getExecutorFailStrategy());
				exists_jobInfo.setChildJobKey(jobInfo.getChildJobKey());
		        jobInfoDao.update(exists_jobInfo);

				// fresh quartz
				String qz_group = String.valueOf(exists_jobInfo.getJobGroup());
				String qz_name = String.valueOf(exists_jobInfo.getId());
		        try {
		            boolean ret = JobDynamicScheduler.rescheduleJob(qz_group, qz_name, exists_jobInfo.getJobCron());
		            return ret?ReturnT.SUCCESS:ReturnT.FAIL;
		        } catch (SchedulerException e) {
		            logger.error("", e);
		        }

				return ReturnT.FAIL;
	}

	@Override
	public ReturnT<String> remove(int id) {
		JobInfo jobInfo = jobInfoDao.loadById(id);
        String group = String.valueOf(jobInfo.getJobGroup());
        String name = String.valueOf(jobInfo.getId());

		try {
			JobDynamicScheduler.removeJob(name, group);
			jobInfoDao.delete(id);
			jobLogDao.delete(id);
			jobLogGlueDao.deleteByJobId(id);
			return ReturnT.SUCCESS;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return ReturnT.FAIL;
	}

	@Override
	public ReturnT<String> pause(int id) {
        JobInfo jobInfo = jobInfoDao.loadById(id);
        String group = String.valueOf(jobInfo.getJobGroup());
        String name = String.valueOf(jobInfo.getId());

		try {
            boolean ret = JobDynamicScheduler.pauseJob(name, group);	// jobStatus do not store
            return ret?ReturnT.SUCCESS:ReturnT.FAIL;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return ReturnT.FAIL;
		}
	}

	@Override
	public ReturnT<String> resume(int id) {
        JobInfo jobInfo = jobInfoDao.loadById(id);
        String group = String.valueOf(jobInfo.getJobGroup());
        String name = String.valueOf(jobInfo.getId());

		try {
			boolean ret = JobDynamicScheduler.resumeJob(name, group);
			return ret?ReturnT.SUCCESS:ReturnT.FAIL;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return ReturnT.FAIL;
		}
	}

	@Override
	public ReturnT<String> triggerJob(int id) {
        JobInfo jobInfo = jobInfoDao.loadById(id);
        String group = String.valueOf(jobInfo.getJobGroup());
        String name = String.valueOf(jobInfo.getId());

		try {
			JobDynamicScheduler.triggerJob(name, group);
			return ReturnT.SUCCESS;
		} catch (SchedulerException e) {
			e.printStackTrace();
			return ReturnT.FAIL;
		}
	}

	@Override
	public Map<String, Object> dashboardInfo() {

		int jobInfoCount = jobInfoDao.findAllCount();
		int jobLogCount = jobLogDao.triggerCountByHandleCode(-1);
		int jobLogSuccessCount = jobLogDao.triggerCountByHandleCode(ReturnT.SUCCESS_CODE);

		// executor count
		Set<String> executerAddressSet = new HashSet<String>();
		List<JobGroup> groupList = jobGroupDao.findAll();

		if (CollectionUtils.isNotEmpty(groupList)) {
			for (JobGroup group: groupList) {
				if (CollectionUtils.isNotEmpty(group.getRegistryList())) {
					executerAddressSet.addAll(group.getRegistryList());
				}
			}
		}

		int executorCount = executerAddressSet.size();

		Map<String, Object> dashboardMap = new HashMap<String, Object>();
		dashboardMap.put("jobInfoCount", jobInfoCount);
		dashboardMap.put("jobLogCount", jobLogCount);
		dashboardMap.put("jobLogSuccessCount", jobLogSuccessCount);
		dashboardMap.put("executorCount", executorCount);
		return dashboardMap;
	}

	@Override
	public ReturnT<Map<String, Object>> triggerChartDate() {
		Date from = DateUtils.addDays(new Date(), -30);
		Date to = new Date();

		List<String> triggerDayList = new ArrayList<String>();
		List<Integer> triggerDayCountSucList = new ArrayList<Integer>();
		List<Integer> triggerDayCountFailList = new ArrayList<Integer>();
		int triggerCountSucTotal = 0;
		int triggerCountFailTotal = 0;

		List<Map<String, Object>> triggerCountMapAll = jobLogDao.triggerCountByDay(from, to, -1);
		List<Map<String, Object>> triggerCountMapSuc = jobLogDao.triggerCountByDay(from, to, ReturnT.SUCCESS_CODE);
		if (CollectionUtils.isNotEmpty(triggerCountMapAll)) {
			for (Map<String, Object> item: triggerCountMapAll) {
				String day = String.valueOf(item.get("triggerDay"));
				int dayAllCount = Integer.valueOf(String.valueOf(item.get("triggerCount")));
				int daySucCount = 0;
				int dayFailCount = dayAllCount - daySucCount;

				if (CollectionUtils.isNotEmpty(triggerCountMapSuc)) {
					for (Map<String, Object> sucItem: triggerCountMapSuc) {
						String daySuc = String.valueOf(sucItem.get("triggerDay"));
						if (day.equals(daySuc)) {
							daySucCount = Integer.valueOf(String.valueOf(sucItem.get("triggerCount")));
							dayFailCount = dayAllCount - daySucCount;
						}
					}
				}

				triggerDayList.add(day);
				triggerDayCountSucList.add(daySucCount);
				triggerDayCountFailList.add(dayFailCount);
				triggerCountSucTotal += daySucCount;
				triggerCountFailTotal += dayFailCount;
			}
		} else {
            for (int i = 4; i > -1; i--) {
                triggerDayList.add(FastDateFormat.getInstance("yyyy-MM-dd").format(DateUtils.addDays(new Date(), -i)));
                triggerDayCountSucList.add(0);
                triggerDayCountFailList.add(0);
            }
		}

		Map<String, Object> result = new HashMap<String, Object>();
		result.put("triggerDayList", triggerDayList);
		result.put("triggerDayCountSucList", triggerDayCountSucList);
		result.put("triggerDayCountFailList", triggerDayCountFailList);
		result.put("triggerCountSucTotal", triggerCountSucTotal);
		result.put("triggerCountFailTotal", triggerCountFailTotal);
		return new ReturnT<Map<String, Object>>(result);
	}

	/**
	 * @Description: 获取任务信息表最大ID值
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	public synchronized ReturnT<Map<String,Integer>> getMaxId() {
		//获取自增长id值
		int maxId= this.jobInfoDao.getMaxId();
		logger.info("获取任务ID值：{}", maxId);
		//修改自增长ID值
		this.jobInfoDao.updateMaxId(maxId);
		Map<String,Integer> paramMap= new HashMap<String,Integer>();
		paramMap.put("maxId", maxId);
		return new ReturnT<Map<String,Integer>>(ReturnT.SUCCESS_CODE, null, paramMap);
	}

	/**
	 * @Description: 获取注册信息list
	 * @param start
	 * @param length
	 * @param executorClient
	 * @param clientName
	 * @return   
	 * @return Map<String,Object>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	public Map<String, Object> pageRegistryList(int start, int length,
			String executorClient, String clientName) {
		// page list
		List<JobRegistry> list = jobRegistryDao.pageList(start, length, executorClient, clientName);
		int list_count = jobRegistryDao.pageListCount(start, length, executorClient, clientName);
		
		// package result
		Map<String, Object> maps = new HashMap<String, Object>();
	    maps.put("recordsTotal", list_count);		// 总记录数
	    maps.put("recordsFiltered", list_count);	// 过滤后的总记录数
	    maps.put("data", list);  					// 分页列表
		return maps;
	}

	/**
	 * @Description: 生成密钥跟令牌
	 * @param id   
	 * @return void  
	 * @throws Exception 
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月28日
	 */
	public ReturnT<String> addSecretKey(int id) throws Exception {
		//根据ID获取注册信息
		JobRegistry registry= this.jobRegistryDao.findById(id);
		//生成密钥
		String aesKey= registry.getRegistryKey() + registry.getRegistryValue() + RandomUtil.getRandomNum(16);
		aesKey= AESUtil.getKeyByPass(aesKey);
		//生成令牌
		String accessToken= registry.getRegistryKey() + "&" + RandomUtil.getRandomNum(16);
		accessToken= AESUtil.aesEncrypt(accessToken, aesKey);
		this.jobRegistryDao.addSecretKey(id,aesKey,accessToken);
		return ReturnT.SUCCESS;
	}

	/**
	 * @Description: 客户端授权或取消授权
	 * @param id
	 * @param ifGrant   
	 * @return void  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月28日
	 */
	public void updateRegistGrant(int id, int ifGrant) {
		this.jobRegistryDao.updateRegistGrant(id,ifGrant);
	}

	/**
	 * @Description: 关联分组
	 * @param id
	 * @param groupId
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月29日
	 */
	public void addRelationGroup(int id, int groupId)throws Exception {
		this.jobRegistryDao.addRelationGroup(id,groupId);
	}
}
