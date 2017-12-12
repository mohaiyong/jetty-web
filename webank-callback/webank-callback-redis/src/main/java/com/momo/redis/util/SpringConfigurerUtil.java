package com.momo.redis.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * ClassName: SpringConfigurerUtil 
 * @Description: Spring属性值操作工具类
 * @author JornTang
 * @date 2017年7月29日
 */
public class SpringConfigurerUtil extends PropertyPlaceholderConfigurer {
	private static Map<String, Object> propertiesMap;

	@Override
	protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {
		super.processProperties(beanFactoryToProcess, props);
		propertiesMap = new HashMap<String, Object>();
		for (Object key : props.keySet()) {
			String keyStr = key.toString();
			String value = props.getProperty(keyStr);
			propertiesMap.put(keyStr, value);
		}
	}

	public static Object getProperty(String key) {
		return propertiesMap.get(key);
	}
}
