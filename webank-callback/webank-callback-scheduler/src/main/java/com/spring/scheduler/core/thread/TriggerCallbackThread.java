package com.spring.scheduler.core.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.scheduler.core.biz.AdminBiz;
import com.spring.scheduler.core.biz.model.HandleCallbackParam;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.executor.JobExecutor;

/**
 * ClassName: TriggerCallbackThread 
 * @Description: 触发回调（心跳调度中心）
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class TriggerCallbackThread {
    private static Logger logger = LoggerFactory.getLogger(TriggerCallbackThread.class);

    private static TriggerCallbackThread instance = new TriggerCallbackThread();
    public static TriggerCallbackThread getInstance(){
        return instance;
    }

    private LinkedBlockingQueue<HandleCallbackParam> callBackQueue = new LinkedBlockingQueue<HandleCallbackParam>();

    private Thread triggerCallbackThread;
    private boolean toStop = false;
    public void start() {
        triggerCallbackThread = new Thread(new Runnable() {

            @Override
            public void run() {
                while(!toStop){
                    try {
                        HandleCallbackParam callback = getInstance().callBackQueue.take();
                        if (callback != null) {

                            // callback list param
                            List<HandleCallbackParam> callbackParamList = new ArrayList<HandleCallbackParam>();
                            int drainToNum = getInstance().callBackQueue.drainTo(callbackParamList);
                            callbackParamList.add(callback);

                            // valid
                            if (JobExecutor.getAdminBizList()==null) {
                                logger.warn(">>>>>>>>>>>>  callback fail, adminAddresses is null, callbackParamList：{}", callbackParamList);
                                continue;
                            }

                            // callback, will retry if error
                            for (AdminBiz adminBiz: JobExecutor.getAdminBizList()) {
                                try {
                                    ReturnT<String> callbackResult = adminBiz.callback(callbackParamList);
                                    if (callbackResult!=null && ReturnT.SUCCESS_CODE == callbackResult.getCode()) {
                                        callbackResult = ReturnT.SUCCESS;
                                        logger.info(">>>>>>>>>>>  callback success, callbackParamList:{}, callbackResult:{}", new Object[]{callbackParamList, callbackResult});
                                        break;
                                    } else {
                                        logger.info(">>>>>>>>>>>  callback fail, callbackParamList:{}, callbackResult:{}", new Object[]{callbackParamList, callbackResult});
                                    }
                                } catch (Exception e) {
                                    logger.error(">>>>>>>>>>>  callback error, callbackParamList：{}", callbackParamList, e);
                                    //getInstance().callBackQueue.addAll(callbackParamList);
                                }
                            }

                        }
                    } catch (Exception e) {
                        logger.error(e.getMessage(), e);
                    }
                }
            }
        });
        triggerCallbackThread.setDaemon(true);
        triggerCallbackThread.start();
    }
    public void toStop(){
        toStop = true;
    }

    public static void pushCallBack(HandleCallbackParam callback){
        getInstance().callBackQueue.add(callback);
        logger.debug(">>>>>>>>>>> , push callback request, logId:{}", callback.getLogId());
    }

}
