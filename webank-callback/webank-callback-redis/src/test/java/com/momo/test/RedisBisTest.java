package com.momo.test;

import java.util.Set;
import java.util.concurrent.Semaphore;

import javax.annotation.Resource;

import net.sourceforge.groboutils.junit.v1.MultiThreadedTestRunner;
import net.sourceforge.groboutils.junit.v1.TestRunnable;

import org.apache.log4j.Logger;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.alibaba.fastjson.JSONObject;
import com.momo.redis.BisRedisSlave;
import com.momo.redis.lock.RedisLockImpl;

/**
 * 单元测试.<br>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:/spring/applicationContext-redis-bis.xml" })
public class RedisBisTest {

	private static Logger logger = Logger.getLogger(RedisBisTest.class);

	@Resource
	private BisRedisSlave bisRedisSlave;

	/**
	 * 抽奖活动单机的最大进入队列.
	 */
	final Semaphore semp = new Semaphore(50);

	/**
	 * 模拟并发10000请求，有十几个是没拿到锁返回人员太多重新发起请求的
	 */
	@Ignore
	@Test
	public void testLotteryDraw() {
		//1.定义测试并发内部类
		TestRunnable runner = new TestRunnable() {
			@Override
			public void runTest() throws Throwable {
				try {
					semp.acquire();//获取队列

					getPrize();//抽奖
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					semp.release();//释放队列
				}
			}
		};

		//2.模拟并发
		startRequest(runner, 10000);
	}

	private void getPrize() {
		String key = "hlq_lock_" + 2017001l;//根据活动ID设置一个锁
		//RedisLockImpl redisLock = new RedisLockImpl(RedisSlave.getRedistemplate());
		RedisLockImpl redisLock = new RedisLockImpl(bisRedisSlave.getRedistemplate());
		boolean lockTag = redisLock.lock(key);
		if (!lockTag) {
			lockTag = redisLock.tryLock(key, 10000);
		}

		//判断是否拿到锁
		if (lockTag) {
			try {
				//业务处理
				System.out.println("==============抽奖中ing...=============");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				redisLock.unLock(key);
			}
		} else {
			System.out.println("目前抽奖人数太多，请稍后重试！");
		}
	}

	private void startRequest(TestRunnable runner, int requestNum) {
		//模拟并发
		int runnerCount = requestNum;
		TestRunnable[] trs = new TestRunnable[runnerCount];//Rnner数组，想当于并发多少个。
		for (int i = 0; i < runnerCount; i++) {
			trs[i] = runner;
		}
		// 用于执行多线程测试用例的Runner，将前面定义的单个Runner组成的数组传入
		MultiThreadedTestRunner mttr = new MultiThreadedTestRunner(trs);
		try {
			mttr.runTestRunnables();// 开发并发执行数组里定义的内容
			Thread.sleep(50 * 10000);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	//缓存一天
	private static final long EXPIRE_TIME = 60 * 60 * 24;

	private static final String REDIS_HEAD = "bis_machine_heart_";

	/**
	 * 1.根据网点设备编码返回Redis中信息
	 */
	public String getRedisInfoByMachineNo(String machineNo) {
		String key = REDIS_HEAD + machineNo;
		String redisHeartStr = bisRedisSlave.getString(key);
		return redisHeartStr;
	}

	/**
	 * 2.根据网点设备编码删除Redis中信息
	 */
	public void delRedisInfoByMachineNo(String machineNo) {
		bisRedisSlave.del(REDIS_HEAD + machineNo);
	}

	/**
	 * 3.根据Redis中key删除信息
	 */
	public void delRedisInfoByKey(String key) {
		bisRedisSlave.del(key);
	}

	/**
	 * 4.返回Redis中所有的Key
	 */
	public Set<String> getAllRedisKeys() {
		Set<String> keys = bisRedisSlave.getRedistemplate().keys("bis_machine_heart_*");
		return keys;
	}

	/**
	 * 5.设置Redis中的信息
	 */
	public void putToRedis(String machineNo, Object redisHeartVo) {
		String key = REDIS_HEAD + machineNo;
		bisRedisSlave.set(key, JSONObject.toJSONString(redisHeartVo), EXPIRE_TIME);
	}

}
