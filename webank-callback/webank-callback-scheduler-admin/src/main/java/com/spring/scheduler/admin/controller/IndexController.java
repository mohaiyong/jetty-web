package com.spring.scheduler.admin.controller;

import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.spring.scheduler.admin.controller.annotation.PermessionLimit;
import com.spring.scheduler.admin.controller.interceptor.PermissionInterceptor;
import com.spring.scheduler.admin.core.util.AESUtil;
import com.spring.scheduler.admin.core.util.PropertiesUtil;
import com.spring.scheduler.admin.service.JobService;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.util.MD5Utils;

/**
 * ClassName: IndexController 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
@Controller
public class IndexController {
	public static void main(String[] args) {
		
		try {
			String ack="ORYBvA0OZSQYzj2oUs5a7ze37eOASKxejU+hme/ZVc3Oh5T6GRHtX0VZEblzIvDDnu7PLGlRY9ZL"+
					"LSjOxX4rHg==";
			String ass="kAbUQZV6TkzdUB++DN3q9oWqZ1c5/tiyucx5mbwz4Fv0R8PBkwBsqqDAOJDOcLWsRz5WXoMjkGAt"+
"8NYSjqIITean8ONF4OMnAaRSHZFXmHArazCbKD8sFX81yuvS/tzYK2ScOC7cBHgdol/jRtjq4g==";
		
		String key="284a4e97f7aea10c2d88c850429dbec5";
			String s= AESUtil.aesDecrypt(ass, key);
			System.err.println(s);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Resource
	private JobService jobService;

	@RequestMapping("/")
	public String index(Model model) {

		Map<String, Object> dashboardMap = jobService.dashboardInfo();
		model.addAllAttributes(dashboardMap);

		return "index";
	}

    @RequestMapping("/triggerChartDate")
	@ResponseBody
	public ReturnT<Map<String, Object>> triggerChartDate() {
        ReturnT<Map<String, Object>> triggerChartDate = jobService.triggerChartDate();
        return triggerChartDate;
    }
	
	@RequestMapping("/toLogin")
	@PermessionLimit(limit=false)
	public String toLogin(Model model, HttpServletRequest request) {
		if (PermissionInterceptor.ifLogin(request)) {
			return "redirect:/";
		}
		return "login";
	}
	
	@RequestMapping(value="login", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> loginDo(HttpServletRequest request, HttpServletResponse response, String userName, String password, String ifRemember){
		if (!PermissionInterceptor.ifLogin(request)) {
			if (StringUtils.isNotBlank(userName) && StringUtils.isNotBlank(password)
					&& PropertiesUtil.getString("login.username").equals(userName)
					&& PropertiesUtil.getString("login.password").equals(MD5Utils.stringToMd5(password))) {
				boolean ifRem = false;
				if (StringUtils.isNotBlank(ifRemember) && "on".equals(ifRemember)) {
					ifRem = true;
				}
				PermissionInterceptor.login(response, request, ifRem);
			} else {
				return new ReturnT<String>(500, "账号或密码错误");
			}
		}
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping(value="logout", method=RequestMethod.POST)
	@ResponseBody
	@PermessionLimit(limit=false)
	public ReturnT<String> logout(HttpServletRequest request, HttpServletResponse response){
		if (PermissionInterceptor.ifLogin(request)) {
			PermissionInterceptor.logout(request, response);
		}
		return ReturnT.SUCCESS;
	}
	
	@RequestMapping("/help")
	public String help() {

		/*if (!PermissionInterceptor.ifLogin(request)) {
			return "redirect:/toLogin";
		}*/

		return "help";
	}
	
}
