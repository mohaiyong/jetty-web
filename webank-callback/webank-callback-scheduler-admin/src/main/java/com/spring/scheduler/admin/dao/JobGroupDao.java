package com.spring.scheduler.admin.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.spring.scheduler.admin.core.model.JobGroup;
import com.spring.scheduler.admin.core.util.AESUtil;

/**
 * ClassName: JobGroupDao 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public interface JobGroupDao {

	public List<JobGroup> findAll();

    public List<JobGroup> findByAddressType(@Param("addressType") int addressType);

    public int save(JobGroup jobGroup);

    public int update(JobGroup jobGroup);

    public int remove(@Param("id") int id);

    public JobGroup load(@Param("id") int id);
}
