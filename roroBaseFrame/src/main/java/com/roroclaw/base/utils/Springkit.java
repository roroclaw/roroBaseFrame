package com.roroclaw.base.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class Springkit {
	/**
	 * 获取当前线程下的request
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes()).getRequest();
		return request;
	}

//	public static DataSourceTransactionManager getTransactionManager(){
//
////		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
//		//事物隔离级别，开启新事务，与A类和B类不使用同一个事务。
////		def.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
//		return transactionManager;
//	}

	/**
	 * 获取WebApplicationContext
	 * @return
	 */
	public static WebApplicationContext getWebApplicationContext(){
		WebApplicationContext webApplicationContext = ContextLoader.getCurrentWebApplicationContext();
		return webApplicationContext;
	}
}
