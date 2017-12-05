package com.roroclaw.base.handler;

import org.springframework.dao.DataAccessException;

public class BizException extends DataAccessException{
	private String statusCode = null;
	
	public BizException(String statusCode,String string) {
		super(string);
		this.statusCode = statusCode;
	}

	public BizException(String string) {
		super(string);
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	
}
