package com.spring.scheduler.admin.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.admin.core.model.JobRegistry;

/**
 * ClassName: JobRegistryDao 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public interface JobRegistryDao {

    public int removeDead(@Param("timeout") int timeout);

    public List<JobRegistry> findAll(@Param("timeout") int timeout);
    
    public JobRegistry findByRegistkey(@Param("registryKey") String registryKey);

    public int registryUpdate(@Param("registryGroup") String registryGroup,
                              @Param("executorClient") String executorClient,
                              @Param("registryValue") String registryValue);

    public int registrySave(@Param("registryGroup") String registryGroup,
                            @Param("registryKey") String registryKey,
                            @Param("registryValue") String registryValue,
                            @Param("executorClient") String executorClient,
                            @Param("clientName") String clientName);

    public List<JobRegistry> pageList(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("executorClient") String executorClient, @Param("clientName") String clientName);

	public int pageListCount(@Param("offset") int offset, @Param("pagesize") int pagesize, @Param("executorClient") String executorClient, @Param("clientName") String clientName);
	/**
	 * @Description: 根据ID获取注册信息
	 * @param id
	 * @return   
	 * @return JobRegistry  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月28日
	 */
	public JobRegistry findById(@Param("id")int id);
	/**
	 * @Description: 生成密钥跟令牌
	 * @param aesKey
	 * @param accessToken   
	 * @return void  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月28日
	 */
	public void addSecretKey(@Param("id")int id, @Param("aesKey")String aesKey,@Param("accessToken") String accessToken);
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
	public void updateRegistGrant(@Param("id")int id,@Param("ifGrant") int ifGrant);
	/**
	 * @Description: 关联分组
	 * @param id
	 * @param groupId   
	 * @return void  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月29日
	 */
	public void addRelationGroup(@Param("id")int id,@Param("groupId") int groupId);
	/**
	 * @Description: 获取所有客户端注册信息
	 * @return   
	 * @return List<JobRegistry>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月29日
	 */
	public List<JobRegistry> findAllRegist();

}
