package com.momo.redis.lock;


/**
 * Redis跨JVM分布式锁
 * 
 * @author zhougw
 *
 */
public interface IRedisLock {
	
	 /**
	 * 获取锁
	 * @param lock
	 *  @return {@code true} 若成功获取到锁, {@code false} 
	 */
	boolean lock(String lock);
	  
	 /**
	 * 释放锁，如果锁已超时
	 * @param lock
	 */
	 void tryUnlock(String lock);
	 /**
	  * 释放锁
	  * 
	  * @param lock
	  */
	 void unLock(String lock);
	 /**
	 * 超时自动返回的阻塞性的获取锁, 不响应中断
	 * 
	 * @param time 单位毫秒
	 * @return {@code true} 若成功获取到锁, {@code false} 若在指定时间内未取到锁
	  * 
	 */
	 boolean tryLock(String lock,long time);
}
