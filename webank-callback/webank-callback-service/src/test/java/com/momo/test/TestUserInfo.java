package com.momo.test;

import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.momo.po.UserInfo;
import com.momo.service.UserInfoService;

/** 
 * 测试 
 * 
 * @author <a href="mailto:mohy@dtds.com.cn">莫海涌</a> 
 * @company 深圳动态网络科技有限公司 版权所有 (c) 2016 
 * @version 2017年12月7日 
 */
public class TestUserInfo extends SpringTestCase {

	@Resource
	private UserInfoService userInfoService;

	@Test
	public void getUserPageList() {
		List<UserInfo> userInfoList = userInfoService.getUserPageList(new UserInfo(), 1, Integer.MAX_VALUE);
		System.out.println(JSON.toJSON(userInfoList));
	}
}
