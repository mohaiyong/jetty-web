package com.spring.scheduler.admin.core.thread;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.scheduler.admin.core.model.JobGroup;
import com.spring.scheduler.admin.core.model.JobInfo;
import com.spring.scheduler.admin.core.model.JobLog;
import com.spring.scheduler.admin.core.schedule.JobDynamicScheduler;
import com.spring.scheduler.admin.core.util.MailUtil;
import com.spring.scheduler.core.biz.model.ReturnT;

/**
 * ClassName: JobFailMonitorHelper 
 * @Description: job monitor instance
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class JobFailMonitorHelper {
	private static Logger logger = LoggerFactory.getLogger(JobFailMonitorHelper.class);
	
	private static JobFailMonitorHelper instance = new JobFailMonitorHelper();
	public static JobFailMonitorHelper getInstance(){
		return instance;
	}

	private LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<Integer>(0xfff8);

	private Thread monitorThread;
	private volatile boolean toStop = false;
	public void start(){
		monitorThread = new Thread(new Runnable() {

			@Override
			public void run() {
				while (!toStop) {
					try {
						logger.debug(">>>>>>>>>>> job monitor beat ... ");
						Integer jobLogId = JobFailMonitorHelper.instance.queue.take();
						if (jobLogId != null && jobLogId > 0) {
							logger.debug(">>>>>>>>>>> job monitor heat success, JobLogId:{}", jobLogId);
							JobLog log = JobDynamicScheduler.jobLogDao.load(jobLogId);
							if (log!=null) {
								if (ReturnT.SUCCESS_CODE==log.getTriggerCode() && log.getHandleCode()==0) {
									// running
									try {
										TimeUnit.SECONDS.sleep(10);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
									JobFailMonitorHelper.monitor(jobLogId);
								}
								if (ReturnT.SUCCESS_CODE==log.getTriggerCode() && ReturnT.SUCCESS_CODE==log.getHandleCode()) {
									// pass
								}
								if (ReturnT.FAIL_CODE == log.getTriggerCode()|| ReturnT.FAIL_CODE==log.getHandleCode()) {
									JobInfo info = JobDynamicScheduler.jobInfoDao.loadById(log.getJobId());
									if (info!=null && info.getAlarmEmail()!=null && info.getAlarmEmail().trim().length()>0) {

										Set<String> emailSet = new HashSet<String>(Arrays.asList(info.getAlarmEmail().split(",")));
										for (String email: emailSet) {
											String title = "《调度监控报警》(任务调度中心)";
											JobGroup group = JobDynamicScheduler.jobGroupDao.load(Integer.valueOf(info.getJobGroup()));
											String content = MessageFormat.format("任务调度失败, 执行器名称:{0}, 任务描述:{1}.", group!=null?group.getTitle():"null", info.getJobDesc());
											MailUtil.sendMail(email, title, content, false, null);
										}
									}
								}
							}
						}
					} catch (Exception e) {
						logger.error("job monitor error:{}", e);
					}
				}
			}
		});
		monitorThread.setDaemon(true);
		monitorThread.start();
	}

	public void toStop(){
		toStop = true;
		//monitorThread.interrupt();
	}
	
	// producer
	public static void monitor(int jobLogId){
		getInstance().queue.offer(jobLogId);
	}
	
}
