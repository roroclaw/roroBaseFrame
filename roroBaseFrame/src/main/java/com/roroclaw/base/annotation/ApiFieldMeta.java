package com.roroclaw.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 返回值属性说明
 * @author dxz
 *
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiFieldMeta {
	
	public String DATA_TYPE_STRING = "string";
	public String DATA_TYPE_INT = "int";
	public String DATA_TYPE_DOUBLE = "double";
	public String DATA_TYPE_DATE = "date";
	public String DATA_TYPE_ARRAY = "array";
	
	/**
	 * 字段名称
	 * @return
	 */
	String name() ;
	/**
	 * 字段描述
	 * @return
	 */
	String description() default "无";
	/**
	 * 字段类型
	 * @return
	 */
	String type();
	
	/**
	 * 字段key
	 * @return
	 */
	String value() ;
	
	/**
	 * 是否必填
	 * @return
	 */
	boolean required() default false;
	
	/**
	 * 是否返回参数
	 * @return
	 */
	boolean isReponse() default true;
	
	/**
	 * 是否请求参数
	 * @return
	 */
	boolean isRequest() default true;
	
	int length() default -1;
	
}
