package com.spring.scheduler.core.util;

import org.apache.commons.lang.text.StrTokenizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * ClassName: IpUtil 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class IpUtil {
	private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);
	
	public static final String _255 = "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";  
	public static final Pattern pattern = Pattern.compile("^(?:" + _255 + "\\.){3}" + _255 + "$");

	private static final String ANYHOST = "0.0.0.0";
	private static final String LOCALHOST = "127.0.0.1";
	public static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

	private static volatile InetAddress LOCAL_ADDRESS = null;

	/**
	 * valid address
	 * @param address
	 * @return
	 */
	private static boolean isValidAddress(InetAddress address) {
		if (address == null || address.isLoopbackAddress())
			return false;
		String name = address.getHostAddress();
		return (name != null
				&& ! ANYHOST.equals(name)
				&& ! LOCALHOST.equals(name)
				&& IP_PATTERN.matcher(name).matches());
	}

	/**
	 * get first valid addredd
	 * @return
	 */
	private static InetAddress getFirstValidAddress() {
		InetAddress localAddress = null;
		try {
			localAddress = InetAddress.getLocalHost();
			if (isValidAddress(localAddress)) {
				return localAddress;
			}
		} catch (Throwable e) {
			logger.error("Failed to retriving ip address, " + e.getMessage(), e);
		}
		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			if (interfaces != null) {
				while (interfaces.hasMoreElements()) {
					try {
						NetworkInterface network = interfaces.nextElement();
						Enumeration<InetAddress> addresses = network.getInetAddresses();
						if (addresses != null) {
							while (addresses.hasMoreElements()) {
								try {
									InetAddress address = addresses.nextElement();
									if (isValidAddress(address)) {
										return address;
									}
								} catch (Throwable e) {
									logger.error("Failed to retriving ip address, " + e.getMessage(), e);
								}
							}
						}
					} catch (Throwable e) {
						logger.error("Failed to retriving ip address, " + e.getMessage(), e);
					}
				}
			}
		} catch (Throwable e) {
			logger.error("Failed to retriving ip address, " + e.getMessage(), e);
		}
		logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
		return localAddress;
	}

	/**
	 * get address
	 * @return
	 */
	private static InetAddress getAddress() {
		if (LOCAL_ADDRESS != null)
			return LOCAL_ADDRESS;
		InetAddress localAddress = getFirstValidAddress();
		LOCAL_ADDRESS = localAddress;
		return localAddress;
	}
	/** 
     * 获取当前网络ip 
     * @param request 
     * @return 
     */  
    public static String getIpAddr(HttpServletRequest request){  
        String ipAddress = request.getHeader("x-forwarded-for");  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {  
                        e.printStackTrace();  
                    }  
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            }  
            return ipAddress;   
    }

	/**
	 * get ip
	 * @return
	 */
	public static String getIp(){
		InetAddress address = getAddress();
		if (address==null) {
			return null;
		}
		return address.getHostAddress();
	}

	/**
	 * get ip:port
	 * @param port
	 * @return
	 */
	public static String getIpPort(int port){
		String ip = getIp();
		if (ip==null) {
			return null;
		}
		return ip.concat(":").concat(String.valueOf(port));
	}

	public static void main(String[] args) throws UnknownHostException {
		System.out.println(getFirstValidAddress());
		System.out.println(getIpPort(8080));
	}
	public static String longToIpV4(long longIp) {  
        int octet3 = (int) ((longIp >> 24) % 256);  
        int octet2 = (int) ((longIp >> 16) % 256);  
        int octet1 = (int) ((longIp >> 8) % 256);  
        int octet0 = (int) ((longIp) % 256);  
        return octet3 + "." + octet2 + "." + octet1 + "." + octet0;  
    }  
  
    public static long ipV4ToLong(String ip) {  
        String[] octets = ip.split("\\.");  
        return (Long.parseLong(octets[0]) << 24) + (Integer.parseInt(octets[1]) << 16)  
                + (Integer.parseInt(octets[2]) << 8) + Integer.parseInt(octets[3]);  
    }  
  
    public static boolean isIPv4Private(String ip) {  
        long longIp = ipV4ToLong(ip);  
        return (longIp >= ipV4ToLong("10.0.0.0") && longIp <= ipV4ToLong("10.255.255.255"))  
                || (longIp >= ipV4ToLong("172.16.0.0") && longIp <= ipV4ToLong("172.31.255.255"))  
                || longIp >= ipV4ToLong("192.168.0.0") && longIp <= ipV4ToLong("192.168.255.255");  
    }  
  
    public static boolean isIPv4Valid(String ip) {  
        return pattern.matcher(ip).matches();  
    }  
  
    public static String getIpFromRequest(HttpServletRequest request) {  
        String ip;  
        boolean found = false;  
        if ((ip = request.getHeader("x-forwarded-for")) != null) {  
            StrTokenizer tokenizer = new StrTokenizer(ip, ",");  
            while (tokenizer.hasNext()) {  
                ip = tokenizer.nextToken().trim();  
                if (isIPv4Valid(ip) && !isIPv4Private(ip)) {  
                    found = true;  
                    break;  
                }  
            }  
        }  
        if (!found) {  
            ip = request.getRemoteAddr();  
        }  
        return ip;  
    }  
    /**
	 * 获取用户IP地址
	 * 
	 * @param request
	 * @return
	 */
	public static String getIpAddrs(final HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("http_client_ip");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		// 如果是多级代理，那么取第一个ip为客户ip
		if (ip != null && ip.indexOf(",") != -1) {
			ip = ip.substring(ip.lastIndexOf(",") + 1, ip.length()).trim();
		}
		if (ip.indexOf("0:") != -1) {
			ip = "本地";
		}
		StringBuffer sb=new StringBuffer();
		sb.append(";x-forwarded-for:"+request.getHeader("x-forwarded-for"));
		sb.append(";Proxy-Client-IP:"+request.getHeader("Proxy-Client-IP"));
		sb.append(";WL-Proxy-Client-IP:"+request.getHeader("WL-Proxy-Client-IP"));
		sb.append("getRemoteAddr:"+request.getRemoteAddr());
		sb.append(";http_client_ip:"+request.getHeader("http_client_ip"));
		sb.append(";HTTP_X_FORWARDED_FOR:"+request.getHeader("HTTP_X_FORWARDED_FOR"));
//		log.error("获取ip："+sb.toString()+"\n头部信息");
		Enumeration he=request.getHeaderNames();
		while(he.hasMoreElements()){
			Object name=he.nextElement();
			String value=request.getHeader(name.toString());
			sb.append("header:"+name);
			sb.append("="+value+",headers:");
			Enumeration e=request.getHeaders(name.toString());
	        while(e.hasMoreElements()){
	           sb.append((String ) e.nextElement()+"------");
	        }
	        sb.append("\n");
		}
		return ip;
	}
}
