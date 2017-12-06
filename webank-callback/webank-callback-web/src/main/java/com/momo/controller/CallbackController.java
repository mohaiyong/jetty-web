package com.momo.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.momo.common.ReturnDTO;

@Controller
@RequestMapping("/webank")
public class CallbackController {

	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 微众：收单回调
	 * @PAY_RESULT 支付回调会重试
	 * @FILE 账单回调
	 * @url POST http://localhost/webank/notify.do
	 */
	@RequestMapping(value = "notify.do", method = { RequestMethod.GET, RequestMethod.POST }, produces = "application/json;charset=utf-8")
	@ResponseBody
	public ReturnDTO notify(HttpServletRequest request) { //4.返回处理成功
		logger.info("================>钱包回调处理成功");
		return ReturnDTO.OK("钱包回调处理成功");
	}

}
