package com.momo.scheduler;

import org.springframework.stereotype.Service;

import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.handler.IJobHandler;
import com.spring.scheduler.core.handler.annotation.JobHander;
import com.spring.scheduler.core.log.JobLogger;
import com.spring.scheduler.core.util.ShardingUtil;


/**
 * ClassName: ShardingJobHandler 
 * @Description: 分片广播任务
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
@JobHander(value="shardingJobHandler")
@Service
public class ShardingJobHandler extends IJobHandler {

	@Override
	public ReturnT<String> execute(String... params) throws Exception {

		// 分片参数
		ShardingUtil.ShardingVO shardingVO = ShardingUtil.getShardingVo();
		JobLogger.log("分片参数：当前分片序号 = {0}, 总分片数 = {1}", shardingVO.getIndex(), shardingVO.getTotal());

		// 业务逻辑
		for (int i = 0; i < shardingVO.getTotal(); i++) {
			if (i == shardingVO.getIndex()) {
				JobLogger.log("第 {0} 片, 命中分片开始处理", i);
			} else {
				JobLogger.log("第 {0} 片, 忽略", i);
			}
		}

		return ReturnT.SUCCESS;
	}
	
}
