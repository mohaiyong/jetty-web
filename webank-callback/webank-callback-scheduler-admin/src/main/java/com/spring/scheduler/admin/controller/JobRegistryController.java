package com.spring.scheduler.admin.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.admin.dao.JobGroupDao;
import com.spring.scheduler.admin.dao.JobRegistryDao;
import com.spring.scheduler.admin.service.JobService;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.util.AESUtil;

/**
 * ClassName: JobInfoController 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
@Controller
@RequestMapping("/jobregistry")
public class JobRegistryController {
	private static Logger logger = LoggerFactory.getLogger(JobRegistryController.class);
	@Resource
	private JobRegistryDao jobRegistryDao;
	@Resource
	private JobGroupDao jobGroupDao;
	@Resource
	private JobService jobService;
	
	@RequestMapping
	public String index(Model model,HttpServletRequest request) {
		return "jobregistry/jobregistry.index";
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length,
			String executorClient, String clientName) {
		
		return jobService.pageRegistryList(start, length, executorClient, clientName);
	}
	/**
	 * @Description: 关联分组
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月29日
	 */
	@RequestMapping(value="/addRelationGroup",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ReturnT<String> addRelationGroup(int id,int groupId){
		ReturnT<String> rt= new ReturnT<String>();
		rt.setMsg("关联分组成功");
		rt.setCode(ReturnT.SUCCESS_CODE);
		try {
			this.jobService.addRelationGroup(id,groupId);
		} catch (Exception e) {
			rt.setCode(ReturnT.FAIL_CODE);
			rt.setMsg("关联分组失败");
			logger.error("关联分组异常",e);
		}
		return rt;
	}
	/**
	 * @Description: 客户端授权或取消授权
	 * @param id
	 * @param ifGrant
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月28日
	 */
	@RequestMapping(value="/updateRegistGrant",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ReturnT<String> updateRegistGrant(int id,int ifGrant){
		ReturnT<String> rt= ReturnT.SUCCESS; 
		try {
			this.jobService.updateRegistGrant(id,ifGrant);
		} catch (Exception e) {
			rt.setCode(ReturnT.FAIL_CODE);
			rt.setMsg("生成密钥跟令牌异常");
			logger.error("生成密钥跟令牌异常",e);
		}
		return rt;
	}
	/**
	 * @Description: 生成密钥跟令牌
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月28日
	 */
	@RequestMapping(value="/addSecretKey",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ReturnT<String> addSecretKey( int id){
		ReturnT<String> rt= ReturnT.SUCCESS; 
		try {
			jobService.addSecretKey(id);
		} catch (Exception e) {
			rt.setCode(ReturnT.FAIL_CODE);
			rt.setMsg("生成密钥跟令牌异常");
			logger.error("生成密钥跟令牌异常",e);
		}
		return rt;
	}
	/**
	 * @Description: 生成密钥跟令牌
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月28日
	 */
	@RequestMapping(value="/secretKeyTest",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ReturnT<String> secretKeyTest(){
		ReturnT<String> rt= ReturnT.SUCCESS; 
		try {
			String accessToken="TVX0V7hpy+hvIv3GT0YqiQWPjiZi/S10AclppGq3Po2+lBTtbA2/XZ1vScsq2QLXBQCGjt9FJoADCT3fdmDt9w==";
			String aesKey="1885f29853de921b8ebec6f2da11baf1";
			String cryAccessToken= AESUtil.aesEncrypt( accessToken+ "&" + System.currentTimeMillis(), aesKey);
			System.err.println("加密前cryAccessToken："+cryAccessToken);
			String eaccessToken= AESUtil.aesDecrypt(accessToken, aesKey);
			System.err.println("解密后cryAccessToken："+cryAccessToken);
		} catch (Exception e) {
			rt.setCode(ReturnT.FAIL_CODE);
			rt.setMsg("AES加密测试失败");
			logger.error("AES加密测试失败",e);
		}
		return rt;
	}
	/**
	 * @Description: 添加任务
	 * @param jobInfo
	 * @return   
	 * @return ReturnT<String> 以JSON格式返回 
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月23日
	 */
	@RequestMapping(value="/add",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ReturnT<String> add(JobInfo jobInfo) {
		return jobService.add(jobInfo);
	}
}
