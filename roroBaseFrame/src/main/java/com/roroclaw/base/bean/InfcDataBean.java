package com.roroclaw.base.bean;

import com.roroclaw.base.utils.Constants;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 开放数据接口统一返回数据模型
 * @author dxzadmin
 *
 */
@XmlRootElement(name = "reslust")
public class InfcDataBean {
	
//	public static String STATUS_SUCCESS = "1"; //调用成功
//	public static String STATUS_FAIL = "0"; //调用失败
//	public static String STATUS_FAIL_USER_VALIDATE = "2"; //用户非法
//	public static String STATUS_OPERATE_VALIDATE = "3"; //操作非法
//
//	public static String DESCRIBE_SUCCESS = "success";
//	public static String DESCRIBE_FAIL = "fail";
	
	private String status = null;			//状态，1成功，0失败
	private String describe = "";		//状态描述
	private Object object = null;       //数据对象,需要返回数据时候使用
	
	
	public String getStatus() {
		return status;
	}
	
	@XmlElement
	public void setStatus(String status) {
		this.status = status;
	}
	
	
	public String getDescribe() {
		return describe;
	}
	@XmlElement
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	
	public Object getObject() {
		return object;
	}
	@XmlElement
	public void setObject(Object object) {
		this.object = object;
	}

	public InfcDataBean(String status, String describe, Object object) {
		super();
		this.status = status;
		this.describe = describe;
		this.object = object;
	}
	
	/**
	 * 默认成功
	 * @param object
	 */
	public InfcDataBean(Object object) {
		super();
		this.status = Constants.EXCEPTION_CODE.STATUS_SUCCESS;
		this.describe = Constants.EXCEPTION_MSG.DESCRIBE_SUCCESS;
		this.object = object;
	}

	public InfcDataBean() {
		super();
	}
	
}
