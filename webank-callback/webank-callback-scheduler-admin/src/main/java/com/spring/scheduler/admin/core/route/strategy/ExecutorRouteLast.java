package com.spring.scheduler.admin.core.route.strategy;

import java.util.ArrayList;

import com.spring.scheduler.admin.core.route.ExecutorRouter;
import com.spring.scheduler.admin.core.trigger.JobTrigger;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.biz.model.TriggerParam;

/**
 * ClassName: ExecutorRouteLast 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class ExecutorRouteLast extends ExecutorRouter {

    public String route(int jobId, ArrayList<String> addressList) {
        return addressList.get(addressList.size()-1);
    }

    @Override
    public ReturnT<String> routeRun(TriggerParam triggerParam, ArrayList<String> addressList) {
        // address
        String address = route(triggerParam.getJobId(), addressList);

        // run executor
        ReturnT<String> runResult = JobTrigger.runExecutor(triggerParam, address);
        runResult.setContent(address);
        return runResult;
    }
}
