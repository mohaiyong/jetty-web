package com.spring.scheduler.admin.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;

import java.io.IOException;
import java.util.Properties;

/**
 * ClassName: PropertiesUtil 
 * @Description: 
 * @author JornTang
 * @email 957707261@qq.com
 * @date 2017年8月17日
 */
public class PropertiesUtil {
	private static Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
	private static final String file_name = "spring-scheduler-admin.properties";


	public static String getString(String key) {
		Properties prop = null;
		try {
			Resource resource = new ClassPathResource(file_name);
			prop = PropertiesLoaderUtils.loadProperties(resource);
		} catch (IOException e) {
			logger.error(e.getMessage(), e);
		}
		if (prop!=null) {
			return prop.getProperty(key);
		}
		return null;
	}
	
	public static void main(String[] args) {
		System.out.println(getString("xxl.job.login.username"));
	}

}
