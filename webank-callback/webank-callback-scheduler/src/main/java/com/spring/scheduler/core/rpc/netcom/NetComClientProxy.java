package com.spring.scheduler.core.rpc.netcom;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.FactoryBean;

import com.spring.scheduler.core.biz.model.TriggerParam;
import com.spring.scheduler.core.rpc.codec.RpcRequest;
import com.spring.scheduler.core.rpc.codec.RpcResponse;
import com.spring.scheduler.core.rpc.netcom.jetty.client.JettyClient;
import com.spring.scheduler.core.util.AESUtil;

/**
 * ClassName: NetComClientProxy 
 * @Description: RPC代理
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class NetComClientProxy implements FactoryBean<Object> {
	private static final Logger logger = LoggerFactory.getLogger(NetComClientProxy.class);

	// ---------------------- config ----------------------
	private Class<?> iface;
	private String serverAddress;
	private String accessToken;
	private JettyClient client = new JettyClient();
	public NetComClientProxy(Class<?> iface, String serverAddress, String accessToken) {
		this.iface = iface;
		this.serverAddress = serverAddress;
		this.accessToken = accessToken;
	}

	@Override
	public Object getObject() throws Exception {
		return Proxy.newProxyInstance(Thread.currentThread()
				.getContextClassLoader(), new Class[] { iface },
				new InvocationHandler() {
					@Override
					public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
						
						//构造请求参数
						RpcRequest request = new RpcRequest();
	                    request.setServerAddress(serverAddress);
	                    request.setCreateMillisTime(System.currentTimeMillis());
	                    request.setAccessToken(accessToken);
	                    request.setClassName(method.getDeclaringClass().getName());
	                    request.setMethodName(method.getName());
	                    request.setParameterTypes(method.getParameterTypes());
	                    request.setParameters(args);
	                    TriggerParam triggerParam= null;
	                    if(args[0] instanceof TriggerParam){
	                    	triggerParam= (TriggerParam)args[0];
	                    	//添加注册key与授权令牌
		            		Map<String,Object> params= new HashMap<String,Object>();
		            		params.put("clientKey", triggerParam.getClientKey());
		            		params.put("accessToken", triggerParam.getAccessToken());
		            		params.put("param", triggerParam.getExecutorParams());
		            		request.setParams(params);
	                    }
	                    // send
	                    RpcResponse response = client.send(request);
	                    
	                    // valid response
						if (response == null) {
							logger.error(">>>>>>>>>>> rpc jetty response not found.");
							throw new Exception(">>>>>>>>>>> rpc jetty response not found.");
						}
	                    if (response.isError()) {
	                        throw new RuntimeException(response.getError());
	                    } else {
	                        return response.getResult();
	                    }
	                   
					}
				});
	}
	@Override
	public Class<?> getObjectType() {
		return iface;
	}
	@Override
	public boolean isSingleton() {
		return false;
	}

}
