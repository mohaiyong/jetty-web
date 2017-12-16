package com.spring.scheduler.core.handler;

import com.spring.scheduler.core.biz.model.ReturnT;

/**
 * ClassName: IJobHandler 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public abstract class IJobHandler {

	/**
	 * job handler
	 * @param params
	 * @return
	 * @throws Exception
	 */
	public abstract ReturnT<String> execute(String... params) throws Exception;
	
}
