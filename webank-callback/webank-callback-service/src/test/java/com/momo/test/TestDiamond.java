package com.momo.test;

import javax.annotation.Resource;

import org.junit.Ignore;
import org.junit.Test;

import com.momo.diamond.DiamondClient;

/** 
 * 测试 
 * 
 * @author <a href="mailto:mohy@dtds.com.cn">莫海涌</a> 
 * @company 深圳动态网络科技有限公司 版权所有 (c) 2016 
 * @version 2017年12月7日 
 */
public class TestDiamond extends SpringTestCase {

	@Resource
	private DiamondClient diamondClient;

	/**
	 * http://127.0.0.1:8080/diamond-server/config.co?dataId=mysql&group=webank_callback
	 */
	@Ignore
	@Test
	public void getDiamond() throws InterruptedException {
		String data = diamondClient.getConfig("dataSource.url");
		System.out.println(data);
		Thread.sleep(10000L);
		data = diamondClient.getConfig("dataSource.url");
		System.out.println(data);
	}
}
