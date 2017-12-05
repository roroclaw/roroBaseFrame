package com.roroclaw.base.handler.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;

import com.roroclaw.base.annotation.ApiFieldMeta;
import com.roroclaw.base.annotation.ApiParam;
import com.roroclaw.base.annotation.NativeInfc;
import com.roroclaw.base.bean.InfcDataBean;
import com.roroclaw.base.bean.PageBean;
import com.roroclaw.base.handler.BizException;

/**
 * 接口组装拦截器AOP
 */
public class InfcContollerAOP {
	private static Logger logger = LoggerFactory
			.getLogger(InfcContollerAOP.class);

	public Object around(ProceedingJoinPoint point) throws Throwable {
		InfcDataBean infcDataBean = null;
		Object[] args = point.getArgs();
		Object target = point.getTarget();
		MethodSignature methodSignature = ((MethodSignature) point
				.getSignature());
		String methodName = methodSignature.getName();
		Class[] parameterTypes = methodSignature.getMethod()
				.getParameterTypes();
		Class<?> classTarget = point.getTarget().getClass();
		Method objMethod = classTarget.getMethod(methodName, parameterTypes);
		NativeInfc nativeInfc = objMethod.getAnnotation(NativeInfc.class);
		try {
			// 检查参数
			this.validateParams(target, methodName, parameterTypes, args);
			Object object = point.proceed(args);
			if (object instanceof ModelAndView) {
				return object;
			}

			if (nativeInfc != null) {
				return object;
			}
			if (object instanceof InfcDataBean) {
				infcDataBean = (InfcDataBean) object;
			} else {
				infcDataBean = new InfcDataBean(object);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return infcDataBean;
	}

	public Object after(JoinPoint jp, Object retVal) throws Throwable {
		InfcDataBean infcDataBean = null;
		try {
			if (retVal instanceof ModelAndView) {
				return retVal;
			}
			if (retVal instanceof InfcDataBean) {
				infcDataBean = (InfcDataBean) retVal;
			} else {
				infcDataBean = new InfcDataBean(retVal);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		return infcDataBean;
	}

	/**
	 * 根据配置的注解验证参数
	 * 
	 * @param methodName
	 * @throws ClassNotFoundException
	 * @throws SecurityException
	 * @throws NoSuchMethodException
	 * @throws BizException
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	private void validateParams(Object target, String methodName,
			Class[] parameterTypes, Object[] paramObjs)
			throws ClassNotFoundException, NoSuchMethodException,
			SecurityException, IllegalArgumentException,
			IllegalAccessException, BizException {
		Method method = target.getClass().getMethod(methodName, parameterTypes);
		Class[] paramTypes = method.getParameterTypes();
		Annotation[][] anno = method.getParameterAnnotations();
		if (anno.length > 0) {
			for (int i = 0; i < anno.length; i++) {
				for (int j = 0; j < anno[i].length; j++) {
					Annotation annoObj = anno[i][j];
					if (annoObj.annotationType().equals(ApiParam.class)) {
						ApiParam apiParam = (ApiParam) anno[i][j];
						// 判断是否pageBean
						Class typeCls = paramTypes[i];
						if (typeCls != PageBean.class) {
							if (typeCls != Boolean.class
									&& typeCls != String.class
									&& typeCls != Integer.class) {//验证对象参数
								this.validateFields(typeCls, paramObjs[i],
										apiParam);
							} else {//验证常用类型
								Object paramObj = paramObjs[i];
								// 验证必填
								if (apiParam.required()) {
									if (paramObj == null || "".equals(paramObj.toString())) {
										throw new BizException("参数[" + apiParam.value() + "]不可以为空!");
									}
								}
								// 验证长度
								int length = apiParam.length();
								this.validateLength(paramObj, length, typeCls,
										apiParam.value());
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 验证必填字段
	 * 
	 * @param object
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws BizException
	 */
	private void validateFields(Class cls, Object object, ApiParam apiParam)
			throws IllegalArgumentException, IllegalAccessException,
			BizException {
		Field[] fields = cls.getDeclaredFields();
		for (Field f : fields) {
			// 获取字段中包含fieldMeta的注解
			ApiFieldMeta meta = f.getAnnotation(ApiFieldMeta.class);
			if (meta != null) {
				f.setAccessible(true);
				// 验证必填
				Object val = f.get(object);
				if (meta.required()) {
					if (val == null) {
						throw new BizException("参数[" + meta.value() + "]不可以为空!");
					} else {
						Class type = f.getType();
						if (type == String.class
								&& "".equals(val.toString().trim())) {
							throw new BizException("参数[" + meta.value()
									+ "]不可以为空!");
						}
					}
				}

				// 这里验证长度
				if (val != null) {
					// 验证长度
					int length = meta.length();
					Class type = f.getType();
					this.validateLength(val, length, type, meta.value());
				}
			}
		}
	}

	private void validateLength(Object valObj, int limitlength, Class type,
			String filedName) throws BizException {
		if (limitlength == -1) {
			return;
		}
		if (type == String.class) {
			int valLen = valObj.toString().length();
			if (valLen > limitlength) {
				throw new BizException("参数[" + filedName + "]超长,限制长度["
						+ limitlength + "]字节!");
			}
		} else if (type == Integer.class) {
			int intVal = (Integer) valObj;
			if (intVal > limitlength) {
				throw new BizException("参数[" + filedName + "]大于限制大小["
						+ limitlength + "]!");
			}
		} else if (type == Double.class) {
			double doubleVal = (Double) valObj;
			if (doubleVal > limitlength) {
				throw new BizException("参数[" + filedName + "]大于限制大小["
						+ limitlength + "]!");
			}
		}
	}

}
