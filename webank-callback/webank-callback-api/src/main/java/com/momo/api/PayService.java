package com.momo.api;

import java.util.List;

import com.momo.po.CustomerVO;

public interface PayService {

	/**
	 * 对外提供API接口
	 * @return
	 */
	public List<CustomerVO> getAllCustomer();
}