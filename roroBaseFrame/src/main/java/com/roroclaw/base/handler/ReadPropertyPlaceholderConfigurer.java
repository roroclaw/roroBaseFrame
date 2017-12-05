package com.roroclaw.base.handler;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

/**
 * 自定义spring的配置读取
 * @author dxz
 *
 */
public class ReadPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer{
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
	   public static Object getContextProperty(String name) {
	       return propertiesMap.get(name);
	   }

}
