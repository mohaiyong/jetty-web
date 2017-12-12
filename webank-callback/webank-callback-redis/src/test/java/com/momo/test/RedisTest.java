package com.momo.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.momo.redis.RedisSlave;

/**
 * ClassName: Main 
 * @Description: 程序入口
 * @author JornTang
 * @date 2017年7月29日
 */
public class RedisTest {
	public void init() {
		FileSystemXmlApplicationContext ctx = new FileSystemXmlApplicationContext("classpath:spring/applicationContext-redis.xml");
		RedisSlave.set("test111111", "666666666", 0);
		System.err.println(RedisSlave.get("test111111"));
	}

	@Test
	public void test() {
		init();
	}

	@Before
	public void before() {
		System.err.println(".........结束.......");
	}

	@After
	public void after() {
		System.err.println(".........开始.......");
	}
}
