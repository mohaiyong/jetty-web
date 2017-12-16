package com.spring.scheduler.admin.controller.interceptor;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.spring.scheduler.admin.controller.JobRegistryController;
import com.spring.scheduler.admin.controller.annotation.PermessionLimit;
import com.spring.scheduler.admin.core.model.JobRegistry;
import com.spring.scheduler.admin.core.util.AESUtil;
import com.spring.scheduler.admin.core.util.CookieUtil;
import com.spring.scheduler.admin.core.util.JacksonUtil;
import com.spring.scheduler.admin.core.util.PropertiesUtil;
import com.spring.scheduler.admin.core.util.SpringContextUtil;
import com.spring.scheduler.admin.dao.JobRegistryDao;
import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.rpc.codec.RpcRequest;
import com.spring.scheduler.core.rpc.codec.RpcResponse;
import com.spring.scheduler.core.rpc.serialize.HessianSerializer;
import com.spring.scheduler.core.util.HttpClientUtil;
import com.spring.scheduler.core.util.IpUtil;

/**
 * ClassName: PermissionInterceptor 
 * @Description: 权限拦截, 简易版
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class PermissionInterceptor extends HandlerInterceptorAdapter {
	
	private static Logger logger = LoggerFactory.getLogger(PermissionInterceptor.class);
	
	public static final String LOGIN_IDENTITY_KEY = "LOGIN_IDENTITY";
	public static final String ACCESS_TOKEN = "accessToken";
	public static final String CLIENT_KEY = "clientKey";
	public static final String LOGIN_IDENTITY_TOKEN;
    static {
        String username = PropertiesUtil.getString("login.username");
        String password = PropertiesUtil.getString("login.password");
        String temp = username + "_" + password;
        LOGIN_IDENTITY_TOKEN = new BigInteger(1, temp.getBytes()).toString(16);
    }
	
	public static boolean login(HttpServletResponse response,HttpServletRequest request, boolean ifRemember){
		CookieUtil.set(response, LOGIN_IDENTITY_KEY, LOGIN_IDENTITY_TOKEN, ifRemember);
		//操作session之后才会产生JSESSIONID
		request.getSession().setAttribute(LOGIN_IDENTITY_KEY, LOGIN_IDENTITY_TOKEN);
		return true;
	}
	public static void logout(HttpServletRequest request, HttpServletResponse response){
		CookieUtil.remove(request, response, LOGIN_IDENTITY_KEY);
	}
	public static boolean ifLogin(HttpServletRequest request){
		String indentityInfo = CookieUtil.getValue(request, LOGIN_IDENTITY_KEY);
		if (indentityInfo==null || !LOGIN_IDENTITY_TOKEN.equals(indentityInfo.trim())) {
			return false;
		}
		return true;
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		ReturnT<String> rt= new ReturnT<String>();
		if (!(handler instanceof HandlerMethod)) {
			return super.preHandle(request, response, handler);
		}
		String header=request.getHeader("user-agent");
		//判断是否携带accessToken
		Map<String, Object> paramsMap= getParameterMap(request);
		//如果是回调api
		if(request.getRequestURI().contains("/api")){
			return super.preHandle(request, response, handler);
		}
		//请求代理
		if(StringUtils.isNotEmpty(header) && header.startsWith("Apache-HttpClient")){
			String clientKey= (String)paramsMap.get(CLIENT_KEY);
			String accessToken= (String)paramsMap.get(ACCESS_TOKEN);
			if(StringUtils.isEmpty(clientKey) && StringUtils.isEmpty(accessToken)){
				rt.setCode(ReturnT.FAIL_CODE);
				rt.setMsg("无权限的HttpClient请求");
				JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
				return false;
			}
		}
		if(paramsMap!= null && paramsMap.size()> 0 ){
			String clientKey= (String)paramsMap.get(CLIENT_KEY);
			String accessToken= (String)paramsMap.get(ACCESS_TOKEN);
			if((StringUtils.isNotEmpty(clientKey) && StringUtils.isEmpty(accessToken)) ||
				(StringUtils.isNotEmpty(accessToken) && StringUtils.isEmpty(clientKey))){
				rt.setCode(ReturnT.FAIL_CODE);
				rt.setMsg("未发现clientKey或accessToken,无权访问");
				JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
				return false;
			}
			JobRegistry registry= null;
			//校验clientKey
			if(StringUtils.isNotEmpty(clientKey)){
				JobRegistryDao registryDao= SpringContextUtil.getBean(JobRegistryDao.class);
				registry= registryDao.findByRegistkey(clientKey);
				logger.info("正在校验客户端KEY:携带的KEY【"+clientKey+"】" + ",验证的KEY【"+registry.getRegistryKey()+"】");
				if(registry== null){
					rt.setCode(ReturnT.FAIL_CODE);
					rt.setMsg("客户端KEY无效");
					JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
					return false;
				}
				if(registry.getIfGrant()== 0){
					rt.setCode(ReturnT.FAIL_CODE);
					rt.setMsg("客户端未授权");
					JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
					return false;
				}
			}
			//校验accessToken
			if(StringUtils.isNotEmpty(accessToken)){
				if(registry!= null){
					String grantAccessToken= registry.getAccessToken().replaceAll("\r", "").replaceAll("\n", "");
					String aesKey= registry.getAesKey();
					logger.info("AES正在解密:accessToken【"+accessToken+"】" + ",aesKey【"+aesKey+"】");
					try {
						accessToken= AESUtil.aesDecrypt(accessToken, aesKey);
					} catch (Exception e) {
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("解密密钥无效");
						JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
						logger.error("AES解密异常",e);
						return false;
					}
					String[] grants= accessToken.split("&");
					if(grants.length< 2){
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("访问的授权令牌格式错误");
						JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
						return false;
					}
					String requestToken= grants[0];
					if(!requestToken.equals(grantAccessToken)){
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("无效的授权令牌");
						JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
						return false;
					}
					//令牌访问的时间限制3分钟
					String requestTime= grants[1];
					if(!StringUtils.isNumeric(requestTime)){
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("访问的授权令牌格式错误");
						JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
						return false;
					}else{
						long requestTimes= Long.valueOf(requestTime);
						long currentTime= System.currentTimeMillis();
						if((currentTime- requestTimes)> 3*60*1000){
							rt.setCode(ReturnT.FAIL_CODE);
							rt.setMsg("无效的授权令牌");
							JacksonUtil.writeJsonString(response,JacksonUtil.writeValueAsString(rt));
							return false;
						}
					}
				}
			}
			if(StringUtils.isNotEmpty(clientKey) && StringUtils.isNotEmpty(accessToken)){
				return super.preHandle(request, response, handler);
			}
		}
		//判断是否登录
		if (!ifLogin(request)) {
			HandlerMethod method = (HandlerMethod)handler;
			PermessionLimit permission = method.getMethodAnnotation(PermessionLimit.class);
			if (permission == null || permission.limit()) {
				response.sendRedirect(request.getContextPath() + "/toLogin");
				//request.getRequestDispatcher("/toLogin").forward(request, response);
				return false;
			}
		}
		
		return super.preHandle(request, response, handler);
	}
	/**
	 * @Description: 获取请求参数Map<String, Object>
	 * @param request
	 * @return   
	 * @return boolean  
	 * @throws
	 * @author JornTang
	 * @email 957707261@qq.com
	 * @date 2017年8月29日
	 */
    public static Map<String, Object> getParameterMap(HttpServletRequest request) {
        Map<String, String[]> properties = request.getParameterMap();//把请求参数封装到Map<String, String[]>中
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<Entry<String, String[]>> iter = properties.entrySet().iterator();
        String name = "";
        String value = "";
        while (iter.hasNext()) {
            Entry<String, String[]> entry = iter.next();
            name = entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) { //用于请求参数中有多个相同名称
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();//用于请求参数中请求参数名唯一
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }
	
}
