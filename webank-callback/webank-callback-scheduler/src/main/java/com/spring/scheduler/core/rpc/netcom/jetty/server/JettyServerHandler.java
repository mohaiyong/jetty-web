package com.spring.scheduler.core.rpc.netcom.jetty.server;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.spring.scheduler.core.biz.model.ReturnT;
import com.spring.scheduler.core.rpc.codec.RpcRequest;
import com.spring.scheduler.core.rpc.codec.RpcResponse;
import com.spring.scheduler.core.rpc.netcom.NetComServerFactory;
import com.spring.scheduler.core.rpc.serialize.HessianSerializer;
import com.spring.scheduler.core.util.AESUtil;
import com.spring.scheduler.core.util.HttpClientUtil;
import com.spring.scheduler.core.util.SpringConfigurerUtil;
import com.spring.scheduler.core.util.StringUtil;

/**
 * ClassName: JettyServerHandler 
 * @Description: jetty handle
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月30日
 */
public class JettyServerHandler extends AbstractHandler {
	private static Logger logger = LoggerFactory.getLogger(JettyServerHandler.class);

	public static final String ACCESS_TOKEN = "accessToken";

	public static final String CLIENT_KEY = "clientKey";

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

		// invoke
		RpcResponse rpcResponse = doInvoke(request);

		// serialize response
		byte[] responseBytes = HessianSerializer.serialize(rpcResponse);

		response.setContentType("text/html;charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		baseRequest.setHandled(true);

		OutputStream out = response.getOutputStream();
		out.write(responseBytes);
		out.flush();

	}

	private RpcResponse doInvoke(HttpServletRequest request) {
		try {
			// deserialize request
			byte[] requestBytes = HttpClientUtil.readBytes(request);
			if (requestBytes == null || requestBytes.length == 0) {
				RpcResponse rpcResponse = new RpcResponse();
				rpcResponse.setError("RpcRequest byte[] is null");
				return rpcResponse;
			}
			RpcRequest rpcRequest = (RpcRequest) HessianSerializer.deserialize(requestBytes, RpcRequest.class);

			// request valida clientKey and accessToken
			String header = request.getHeader("user-agent");
			Map<String, Object> paramsMap = rpcRequest.getParams();
			ReturnT<String> rt = new ReturnT<String>();
			RpcResponse rpcResponse = new RpcResponse();
			//请求代理
			if (StringUtil.isNotEmpty(header) && header.startsWith("Apache-HttpClient")) {
				String clientKey = (String) paramsMap.get(CLIENT_KEY);
				String accessToken = (String) paramsMap.get(ACCESS_TOKEN);
				if (StringUtil.isEmpty(clientKey) && StringUtil.isEmpty(accessToken)) {
					rt.setCode(ReturnT.FAIL_CODE);
					rt.setMsg("无权限的HttpClient请求");
					rpcResponse.setResult(rt);
					return rpcResponse;
				}
			}
			if (paramsMap != null && paramsMap.size() > 0) {
				String clientKey = (String) paramsMap.get(CLIENT_KEY);
				String accessToken = (String) paramsMap.get(ACCESS_TOKEN);
				String aesKey = SpringConfigurerUtil.getProperty("aesKey");
				if ((StringUtil.isNotEmpty(clientKey) && StringUtil.isEmpty(accessToken))
						|| (StringUtil.isNotEmpty(accessToken) && StringUtil.isEmpty(clientKey))) {
					rt.setCode(ReturnT.FAIL_CODE);
					rt.setMsg("未发现clientKey或accessToken,无权访问");
					rpcResponse.setResult(rt);
					return rpcResponse;
				}
				//校验clientKey
				String registClientKey = null;
				if (StringUtil.isNotEmpty(clientKey)) {
					registClientKey = SpringConfigurerUtil.getProperty("clientKey");
					logger.info("正在校验客户端KEY:携带的KEY【" + clientKey + "】" + ",验证的KEY【" + registClientKey + "】");
					if (!registClientKey.equals(clientKey)) {
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("客户端KEY无效");
						rpcResponse.setResult(rt);
						return rpcResponse;
					}
				}
				//校验accessToken
				String grantAccessToken = null;
				if (StringUtil.isNotEmpty(accessToken)) {
					grantAccessToken = SpringConfigurerUtil.getProperty("accessToken");
					try {
						accessToken = AESUtil.aesDecrypt(accessToken, aesKey);
					} catch (Exception e) {
						e.printStackTrace();
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("解密密钥无效");
						rpcResponse.setResult(rt);
						return rpcResponse;
					}
					String[] grants = accessToken.split("&");
					if (grants.length < 2) {
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("访问的授权令牌格式错误");
						rpcResponse.setResult(rt);
						return rpcResponse;
					}
					String requestToken = grants[0].replaceAll("\r", "").replaceAll("\n", "");
					if (!requestToken.equals(grantAccessToken)) {
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("无效的授权令牌");
						rpcResponse.setResult(rt);
						return rpcResponse;
					}
					//令牌访问的时间限制3分钟
					String requestTime = grants[1];
					if (!StringUtil.isNumeric(requestTime)) {
						rt.setCode(ReturnT.FAIL_CODE);
						rt.setMsg("访问的授权令牌格式错误");
						rpcResponse.setResult(rt);
						return rpcResponse;
					} else {
						long requestTimes = Long.valueOf(requestTime);
						long currentTime = System.currentTimeMillis();
						if ((currentTime - requestTimes) > 3 * 60 * 1000) {
							rt.setCode(ReturnT.FAIL_CODE);
							rt.setMsg("无效的授权令牌");
							rpcResponse.setResult(rt);
							return rpcResponse;
						}
					}
				}
			}
			// invoke
			rpcResponse = NetComServerFactory.invokeService(rpcRequest, null);
			return rpcResponse;
		} catch (Exception e) {
			logger.error(e.getMessage(), e);

			RpcResponse rpcResponse = new RpcResponse();
			rpcResponse.setError("Server-error:" + e.getMessage());
			return rpcResponse;
		}

	}

}
