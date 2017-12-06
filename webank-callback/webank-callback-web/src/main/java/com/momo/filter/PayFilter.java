package com.momo.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.momo.util.NetworkUtil;
import com.momo.util.RequestUtil;

@Component
public class PayFilter implements Filter {

	private static final Logger logger = LoggerFactory.getLogger(PayFilter.class);

	private List<String> filterUrlList;//需要过滤的URL的列表

	/**
	 * 拦截器
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
		//1.获取相关请求参数
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		String requestIp = NetworkUtil.getIpAddress(httpRequest);//请求IP
		String requestURL = httpRequest.getRequestURL().toString();//完整包含域名的请求
		String requestURI = httpRequest.getRequestURI();//请求的方法
		String client = NetworkUtil.getAgent(httpRequest);
		Map<String, String> params = RequestUtil.getRequestParams(httpRequest);

		//2.判断该请求是否被配置拦截
		if (filterUrlList.contains(requestURI)) {
		} else {
			chain.doFilter(request, response);
			return;
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		String filterUrls = filterConfig.getInitParameter("filterUrls");
		String[] filterUrlsArray = filterUrls.split(",");
		filterUrlList = new ArrayList<String>();
		for (int i = 0; i < filterUrlsArray.length; i++) {
			filterUrlList.add(filterUrlsArray[i].trim());
		}
		logger.info("===========================> 初始化Pay Filter: filterUrlList=" + JSON.toJSONString(filterUrlList));
	}

	@Override
	public void destroy() {
		logger.info("===========================> 销毁Pay Filter");
	}

}