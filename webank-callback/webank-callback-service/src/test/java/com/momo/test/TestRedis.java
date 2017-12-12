package com.momo.test;

import javax.annotation.Resource;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.momo.po.UserInfo;
import com.momo.redis.BisRedisSlave;

/** 
 * 测试 
 * 
 * @author <a href="mailto:mohy@dtds.com.cn">莫海涌</a> 
 * @company 深圳动态网络科技有限公司 版权所有 (c) 2016 
 * @version 2017年12月7日 
 */
public class TestRedis extends SpringTestCase {

	@Resource
	private BisRedisSlave bisRedisSlave;

	@Test
	public void getUserPageList() {
		UserInfo userInfo = new UserInfo();
		userInfo.setUsername("张三");
		bisRedisSlave.set("momo", JSONObject.toJSONString(userInfo), 60 * 60 * 24);//缓存一天
		System.out.println(bisRedisSlave.getString("momo"));
	}
}
