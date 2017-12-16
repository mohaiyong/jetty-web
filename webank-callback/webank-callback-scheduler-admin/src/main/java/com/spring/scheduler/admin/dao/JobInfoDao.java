package com.spring.scheduler.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.scheduler.admin.core.model.JobInfo;


/**
 * ClassName: JobInfoDao 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public interface JobInfoDao {

	public List<JobInfo> pageList(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("jobGroup") int jobGroup, @Param("executorHandler") String executorHandler);
	public int pageListCount(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("jobGroup") int jobGroup, @Param("executorHandler") String executorHandler);
	
	public int save(JobInfo info);

	public JobInfo loadById(@Param("id") int id);
	
	public int update(JobInfo item);
	
	public int delete(@Param("id") int id);

	public List<JobInfo> getJobsByGroup(@Param("jobGroup") int jobGroup);

	public int findAllCount();
	/**
	 * @Description: 获取自增长id值
	 * @return   
	 * @return int  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	public int getMaxId();
	/**
	 * @Description: 修改自增长ID值
	 * @param maxId   
	 * @return void  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	public void updateMaxId(int maxId);

}
