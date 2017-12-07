package com.momo.service.api;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.momo.api.PayService;
import com.momo.po.CustomerVO;
import com.momo.service.UserInfoService;

/** 
 * 对外提供外部Dubbo 
 * 
 * @author <a href="mailto:mohy@dtds.com.cn">莫海涌</a> 
 * @company 深圳动态网络科技有限公司 版权所有 (c) 2016 
 * @version 2017年11月24日 
 */
@Service("payServiceImpl")
public class PayServiceImpl implements PayService {

	@Resource
	private UserInfoService userInfoService;

	@Override
	public List<CustomerVO> getAllCustomer() {
		return null;
	}

}