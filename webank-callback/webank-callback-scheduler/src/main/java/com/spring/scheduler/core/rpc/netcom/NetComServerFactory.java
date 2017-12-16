package com.spring.scheduler.core.rpc.netcom;

import java.util.HashMap;
import java.util.Map;

import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.rpc.codec.RpcRequest;
import com.spring.scheduler.core.rpc.codec.RpcResponse;
import com.spring.scheduler.core.rpc.netcom.jetty.server.JettyServer;

/**
 * netcom init
 * @author xuxueli 2015-10-31 22:54:27
 */
public class NetComServerFactory  {
	private static final Logger logger = LoggerFactory.getLogger(NetComServerFactory.class);

	// ---------------------- server start ----------------------
	JettyServer server = new JettyServer();
	public void start(int port, String ip, String client, String clientName) throws Exception {
		server.start(port, ip, client, clientName);
	}

	// ---------------------- server destroy ----------------------
	public void destroy(){
		server.destroy();
	}

	// ---------------------- server instance ----------------------
	/**
	 * init local rpc service map
	 */
	private static Map<String, Object> serviceMap = new HashMap<String, Object>();
	private static String accessToken;
	public static void putService(Class<?> iface, Object serviceBean){
		serviceMap.put(iface.getName(), serviceBean);
	}
	public static void setAccessToken(String accessToken) {
		NetComServerFactory.accessToken = accessToken;
	}
	public static RpcResponse invokeService(RpcRequest request, Object serviceBean) {
		if (serviceBean==null) {
			serviceBean = serviceMap.get(request.getClassName());
		}
		if (serviceBean == null) {
			// TODO
		}

		RpcResponse response = new RpcResponse();
		
		//
		if (System.currentTimeMillis() - request.getCreateMillisTime() > 180000) {
			response.setResult(new ReturnT<String>(ReturnT.FAIL_CODE, "The timestamp difference between admin and executor exceeds the limit."));
			return response;
		}
		if (accessToken!=null && accessToken.trim().length()>0 && !accessToken.trim().equals(request.getAccessToken())) {
			response.setResult(new ReturnT<String>(ReturnT.FAIL_CODE, "The access token[" + request.getAccessToken() + "] is wrong."));
			return response;
		}

		try {
			Class<?> serviceClass = serviceBean.getClass();
			String methodName = request.getMethodName();
			Class<?>[] parameterTypes = request.getParameterTypes();
			Object[] parameters = request.getParameters();

			FastClass serviceFastClass = FastClass.create(serviceClass);
			FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);

			Object result = serviceFastMethod.invoke(serviceBean, parameters);

			response.setResult(result);
		} catch (Throwable t) {
			t.printStackTrace();
			response.setError(t.getMessage());
		}

		return response;
	}

}
