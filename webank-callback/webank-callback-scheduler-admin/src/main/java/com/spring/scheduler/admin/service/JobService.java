package com.spring.scheduler.admin.service;


import java.util.Map;

import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.core.biz.model.ReturnT;

/**
 * ClassName: JobService 
 * @Description: core job action for
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public interface JobService {
	
	public Map<String, Object> pageList(int start, int length, int jobGroup, String executorHandler, String filterTime);
	
	public ReturnT<String> add(JobInfo jobInfo);
	
	public ReturnT<String> reschedule(JobInfo jobInfo);
	
	public ReturnT<String> remove(int id);
	
	public ReturnT<String> pause(int id);
	
	public ReturnT<String> resume(int id);
	
	public ReturnT<String> triggerJob(int id);

	public Map<String,Object> dashboardInfo();

	public ReturnT<Map<String,Object>> triggerChartDate();
	/**
	 * @Description: 获取任务信息表最大ID值
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	public ReturnT<Map<String,Integer>> getMaxId();
	
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
			String executorClient, String clientName);
	/**
	 * @Description: 生成密钥跟令牌
	 * @param id   
	 * @return void  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月28日
	 */
	public ReturnT<String> addSecretKey(int id)throws Exception;
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
	public void updateRegistGrant(int id, int ifGrant);
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
	public void addRelationGroup(int id, int groupId)throws Exception;

}
