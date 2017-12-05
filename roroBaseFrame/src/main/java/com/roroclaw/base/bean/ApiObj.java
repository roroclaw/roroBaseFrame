package com.roroclaw.base.bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * api 对象
 * 
 * @author dxz
 * 
 */
public class ApiObj {
	public String packageName = "";
	public String apiName = "";
	public String apiValue = "";
	public String apiRemark = "";
	public String url = "";
	public List paramList = new ArrayList();
	public List returnList = new ArrayList();
	
	public static String PARAM_NAME = "name";
	public static String PARAM_TYPE = "type";
	public static String PARAM_REQUIRED = "required";
	public static String PARAM_VALUE = "value";
	public static String PARAM_LENGTH = "length";
	
	public static String RETURN_NAME = "name";
	public static String RETURN_TYPE = "type";
	public static String RETURN_VALUE = "value";
	public static String RETURN_DEC = "description";
	

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List getParamList() {
		return paramList;
	}

	public void setParamList(List paramList) {
		this.paramList = paramList;
	}
	
	public void addParamItem(Map paramMap){
		this.paramList.add(paramMap);
	}
	
	public void addParamItems(List paramList){
		this.paramList.addAll(paramList);
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getApiValue() {
		return apiValue;
	}

	public void setApiValue(String apiValue) {
		this.apiValue = apiValue;
	}

	public String getApiRemark() {
		return apiRemark;
	}

	public void setApiRemark(String apiRemark) {
		this.apiRemark = apiRemark;
	}

	public List getReturnList() {
		return returnList;
	}

	public void setReturnList(List returnList) {
		this.returnList = returnList;
	}

}