package com.spring.scheduler.admin.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.scheduler.admin.core.enums.ExecutorFailStrategyEnum;
import com.spring.scheduler.admin.core.model.JobGroup;
import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.admin.core.route.ExecutorRouteStrategyEnum;
import com.spring.scheduler.admin.core.util.CookieUtil;
import com.spring.scheduler.admin.dao.JobGroupDao;
import com.spring.scheduler.admin.service.JobService;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.enums.ExecutorBlockStrategyEnum;
import com.spring.scheduler.core.glue.GlueTypeEnum;

/**
 * ClassName: JobInfoController 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
@Controller
@RequestMapping("/jobinfo")
public class JobInfoController {

	@Resource
	private JobGroupDao jobGroupDao;
	@Resource
	private JobService jobService;
	
	@RequestMapping
	public String index(Model model, @RequestParam(required = false, defaultValue = "-1") int jobGroup) {

		//枚举-字典
		model.addAttribute("ExecutorRouteStrategyEnum", ExecutorRouteStrategyEnum.values());	// 路由策略-列表
		model.addAttribute("GlueTypeEnum", GlueTypeEnum.values());								// Glue类型-字典
		model.addAttribute("ExecutorBlockStrategyEnum", ExecutorBlockStrategyEnum.values());	// 阻塞处理策略-字典
		model.addAttribute("ExecutorFailStrategyEnum", ExecutorFailStrategyEnum.values());		// 失败处理策略-字典

		// 任务组
		List<JobGroup> jobGroupList =  jobGroupDao.findAll();
		model.addAttribute("JobGroupList", jobGroupList);
		model.addAttribute("jobGroup", jobGroup);

		return "jobinfo/jobinfo.index";
	}
	
	@RequestMapping("/pageList")
	@ResponseBody
	public Map<String, Object> pageList(@RequestParam(required = false, defaultValue = "0") int start,  
			@RequestParam(required = false, defaultValue = "10") int length,
			int jobGroup, String executorHandler, String filterTime) {
		
		return jobService.pageList(start, length, jobGroup, executorHandler, filterTime);
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
	@RequestMapping(value="/jsonp",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public void jsonp(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.getWriter().write("jsonp" + "(" + "{msg:\"测试jonsp\",stat: true}" + ")");
		} catch (IOException e) {
			e.printStackTrace();
		} //返回jsonp数据  
	}
	/**
	 * @Description: 获取任务信息表最大ID值
	 * @param jobInfo
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	@RequestMapping(value="/getMaxId",produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public ReturnT<Map<String,Integer>> getMaxId() {
		return jobService.getMaxId();
	}
	/**
	 * @Description: 修改任务信息
	 * @param jobInfo
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	@RequestMapping("/reschedule")
	@ResponseBody
	public ReturnT<String> reschedule(JobInfo jobInfo) {
		return jobService.reschedule(jobInfo);
	}
	
	/**
	 * @Description: 删除
	 * @param id
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	@RequestMapping("/remove")
	@ResponseBody
	public ReturnT<String> remove(int id) {
		return jobService.remove(id);
	}
	
	/**
	 * @Description: 暂停
	 * @param id
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	@RequestMapping("/pause")
	@ResponseBody
	public ReturnT<String> pause(int id) {
		return jobService.pause(id);
	}
	
	/**
	 * @Description: 恢复
	 * @param id
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	@RequestMapping("/resume")
	@ResponseBody
	public ReturnT<String> resume(int id) {
		return jobService.resume(id);
	}
	
	/**
	 * @Description: 执行
	 * @param id
	 * @return   
	 * @return ReturnT<String>  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月25日
	 */
	@RequestMapping("/trigger")
	@ResponseBody
	public ReturnT<String> triggerJob(int id) {
		return jobService.triggerJob(id);
	}
	
}
