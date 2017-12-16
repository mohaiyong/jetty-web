package com.spring.scheduler.core.handler.impl;

import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.handler.IJobHandler;
import com.spring.scheduler.core.log.JobLogger;

/**
 * ClassName: GlueJobHandler 
 * @Description: 
 * @author JornTang
 * @date 2017年8月17日
 */
public class GlueJobHandler extends IJobHandler {

	private long glueUpdatetime;
	private IJobHandler jobHandler;
	public GlueJobHandler(IJobHandler jobHandler, long glueUpdatetime) {
		this.jobHandler = jobHandler;
		this.glueUpdatetime = glueUpdatetime;
	}
	public long getGlueUpdatetime() {
		return glueUpdatetime;
	}

	@Override
	public ReturnT<String> execute(String... params) throws Exception {
		JobLogger.log("----------- glue.version:"+ glueUpdatetime +" -----------");
		return jobHandler.execute(params);
	}

}
