package com.roroclaw.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口方法说明
 * 
 * @author dxz
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiParam {
	public String DATA_TYPE_STRING = "string";
	public String DATA_TYPE_INT = "int";
	public String DATA_TYPE_DATE = "date";
	public String DATA_TYPE_DOUBLE = "double";
	public String DATA_TYPE_ARRAY = "array";

	public String name();

//	public Class type();

	public boolean required() default false;

	public String value();

	public int length() default -1;

	public String[] pageQueryParams() default {};
	
//	public String[] pageQueryParamsValue() default {};
//
//	public String[] pageQueryParamsName() default {};
}
