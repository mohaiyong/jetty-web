package com.spring.scheduler.admin.core.jobbean.impl;
//package com.action.job.impl;
//
//import java.util.concurrent.TimeUnit;
//
//import org.quartz.DisallowConcurrentExecution;
//
//import com.action.job.LocalNomalJobBean;
//
///**
// * demo job bean for no-concurrent
// * @author xuxueli 2016-3-12 14:25:14
// */
//@Deprecated
//@DisallowConcurrentExecution	// ���У��߳���Ҫ�����ü�����������Ч��
//public class DemoConcurrentJobBean extends LocalNomalJobBean {
//
//	@Override
//	public Object handle(String... param) {
//		
//		try {
//			TimeUnit.SECONDS.sleep(10);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//		
//		return false;
//	}
//
//}
