package com.roroclaw.base.service;

import com.roroclaw.base.utils.Springkit;

import java.util.List;
import java.util.Map;


public class BaseService {
	
	/**
	 * 从结果list中获取查询的制定值 
	 * @param list
	 * @param key
	 * @return
	 */
	public Object getResObj4List(List list,String key){
		Object obj = null;
		if(list.size() > 0){
			Map resMap = (Map) list.get(0);
			obj = resMap.get(key);
		}
		return obj;
	}
	/**
	 * 从结果list中获取查询的制定值 
	 * @param list
	 * @param key
	 * @return
	 */
	public String getResStr4List(List list,String key){
		String obj = "";
		if(list.size() > 0){
			Map resMap = (Map) list.get(0);
			obj = (String)resMap.get(key);
		}
		return obj;
	}

	public Object getCurProxyBean(String beanName){
		return Springkit.getWebApplicationContext().getBean(beanName);
	}
}
