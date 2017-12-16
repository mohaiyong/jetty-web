package com.spring.scheduler.core.biz;

import java.util.List;

import com.spring.scheduler.core.biz.model.HandleCallbackParam;
import com.spring.scheduler.core.biz.model.RegistryParam;
import com.spring.scheduler.core.biz.model.ReturnT;

/**
 * ClassName: AdminBiz 
 * @Description: 
 * @author JornTang
 * @date 2017年8月17日
 */
public interface AdminBiz {

    public static final String MAPPING = "/api";

    /**
     * callback
     *
     * @param callbackParamList
     * @return
     */
    public ReturnT<String> callback(List<HandleCallbackParam> callbackParamList);

    /**
     * registry
     *
     * @param registryParam
     * @return
     */
    public ReturnT<String> registry(RegistryParam registryParam);

}
