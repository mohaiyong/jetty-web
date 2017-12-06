package com.momo.controller;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.momo.common.ReturnDTO;
import com.momo.po.UserInfo;
import com.momo.service.UserInfoService;

@Controller
@RequestMapping("/")
public class UserInfoController {

	@Resource
	private UserInfoService userInfoService;

	@RequestMapping("/")
	public String index() {
		return "index";
	}

	/**
	 * 查询所有用户： http://localhost/getUserList.do?key=一休
	 * @param username
	 * @return
	 */
	@RequestMapping("getUserList.do")
	@ResponseBody
	public ReturnDTO getUserList(String key) { //4.返回处理成功
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername(key);
		List<UserInfo> userList = userInfoService.getUserPageList(userInfo, 0, Integer.MAX_VALUE);
		return ReturnDTO.OK(userList);
	}

}