package com.spring.scheduler.core.biz.model;

import java.io.Serializable;

/**
 * ClassName: HandleCallbackParam 
 * @Description: 
 * @author JornTang
 * @date 2017年8月17日
 */
public class HandleCallbackParam implements Serializable {
    private static final long serialVersionUID = 42L;

    private int logId;
    private ReturnT<String> executeResult;

    public HandleCallbackParam(){}
    public HandleCallbackParam(int logId, ReturnT<String> executeResult) {
        this.logId = logId;
        this.executeResult = executeResult;
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public ReturnT<String> getExecuteResult() {
        return executeResult;
    }

    public void setExecuteResult(ReturnT<String> executeResult) {
        this.executeResult = executeResult;
    }

    @Override
    public String toString() {
        return "HandleCallbackParam{" +
                "logId=" + logId +
                ", executeResult=" + executeResult +
                '}';
    }
}
