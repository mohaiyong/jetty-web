package com.spring.scheduler.admin.core.route.strategy;

import java.util.ArrayList;

import com.spring.scheduler.admin.core.route.ExecutorRouter;
import com.spring.scheduler.admin.core.schedule.JobDynamicScheduler;
import com.spring.scheduler.admin.core.trigger.JobTrigger;
import com.spring.scheduler.core.biz.ExecutorBiz;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.biz.model.TriggerParam;

/**
 * ClassName: ExecutorRouteBusyover 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class ExecutorRouteBusyover
  extends ExecutorRouter
{
  public String route(int jobId, ArrayList<String> addressList)
  {
    return (String)addressList.get(0);
  }
  
  public ReturnT<String> routeRun(TriggerParam triggerParam, ArrayList<String> addressList)
  {
    StringBuffer idleBeatResultSB = new StringBuffer();
    for (String address : addressList)
    {
      ReturnT<String> idleBeatResult = null;
      try
      {
        ExecutorBiz executorBiz = JobDynamicScheduler.getExecutorBiz(address);
        idleBeatResult = executorBiz.idleBeat(triggerParam.getJobId());
      }
      catch (Exception e)
      {
        logger.error(e.getMessage(), e);
        idleBeatResult = new ReturnT(500, "" + e);
      }
      idleBeatResultSB.append( (idleBeatResultSB.length()>0)?"<br><br>":"")
      .append("空闲检测：")
      .append("<br>address：").append(address)
      .append("<br>code：").append(idleBeatResult.getCode())
      .append("<br>msg：").append(idleBeatResult.getMsg());
      if (idleBeatResult.getCode() == 200)
      {
        ReturnT<String> runResult = JobTrigger.runExecutor(triggerParam, address);
        idleBeatResultSB.append("<br><br>").append(runResult.getMsg());
        

        runResult.setMsg(idleBeatResultSB.toString());
        runResult.setContent(address);
        return runResult;
      }
    }
    return new ReturnT(500, idleBeatResultSB.toString());
  }
}
