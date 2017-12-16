package com.spring.scheduler.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.scheduler.admin.core.model.JobLogGlue;

/**
 * ClassName: JobLogGlueDao 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public interface JobLogGlueDao {
	
	public int save(JobLogGlue jobLogGlue);
	
	public List<JobLogGlue> findByJobId(@Param("jobId") int jobId);

	public int removeOld(@Param("jobId") int jobId, @Param("limit") int limit);

	public int deleteByJobId(@Param("jobId") int jobId);
	
}
