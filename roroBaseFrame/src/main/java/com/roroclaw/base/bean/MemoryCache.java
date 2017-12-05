package com.roroclaw.base.bean;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


import org.apache.commons.codec.EncoderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;



/**
 * 基础数据缓存类 主要是为了缓存系统中变化很少，却又经常使用的那一部分基础数据，以达到系统加速的目的
 * 
 * @author dxz
 * 
 */
public class MemoryCache {

	private static Logger logger = LoggerFactory.getLogger(MemoryCache.class);

	private Properties interfaceProperties;
	private Properties sysConfigProperties;
	
	private static Map<String, String> interfaceConfig = new HashMap<String, String>();
	private static Map<String, String> sysConfig = new HashMap<String, String>();

	
	/**
	 * 从配置文件取数据初始化缓存
	 */
	private void initInterfaceConfig() {
		logger.info("初始化接口配置信息....");
		Set<Entry<Object, Object>> entrySet = interfaceProperties.entrySet();
		for (Iterator<Entry<Object, Object>> iterator = entrySet.iterator(); iterator
				.hasNext();) {
			Entry<Object, Object> entry = iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			interfaceConfig.put(key, value);
		}
		 logger.info("初始化接口配置信息结束....");
	}
	
	/**
	 * 从配置文件获取数据初始化缓存
	 */
	private void initSysConfig() {
		logger.info("初始化系统配置信息....");
		Set<Entry<Object, Object>> entrySet = sysConfigProperties.entrySet();
		for (Iterator<Entry<Object, Object>> iterator = entrySet.iterator(); iterator
				.hasNext();) {
			Entry<Object, Object> entry = iterator.next();
			String key = (String) entry.getKey();
			String value = (String) entry.getValue();
			sysConfig.put(key, value);
			logger.debug(key+"="+value);
		}
		logger.info("初始化系统配置信息结束....");
	}
	

	public void setInterfaceLocation(Resource interfaceLocation) {
		try {
			EncodedResource encodedResource = new EncodedResource(interfaceLocation, "utf-8");
			interfaceProperties = PropertiesLoaderUtils.loadProperties(encodedResource);
			initInterfaceConfig();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}
	
	public void setSysConfigLocation(Resource sysConfigLocation) {
		try {
			EncodedResource encodedResource = new EncodedResource(sysConfigLocation, "utf-8");
			sysConfigProperties = PropertiesLoaderUtils.loadProperties(encodedResource);
			initSysConfig();
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
	}

	/**
	 * 获取接口配置
	 * @param key
	 * @return
	 */
	public static String getInterfaceKey(String key){
		String keyStr = "";
		keyStr = interfaceConfig.get(key);
		return keyStr;
	}
	
	/**
	 * 获取系统配置
	 * @param key
	 * @return
	 */
	public static String getSysConfigKey(String key){
		String keyStr = "";
		keyStr = sysConfig.get(key);
		return keyStr;
	}

}
