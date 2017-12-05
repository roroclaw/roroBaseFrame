package com.roroclaw.base.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 接口方法说明
 * @author dxz
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiOperation {
	public String name();
//	public boolean isPageBean() default false;
	public Class resCls();
	public String remark() default "";
	public String[] resParam() default {};
	
}
