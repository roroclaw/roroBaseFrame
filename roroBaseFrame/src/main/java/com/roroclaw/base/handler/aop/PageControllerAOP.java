package com.roroclaw.base.handler.aop;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.WebRequest;

import com.roroclaw.base.bean.InfcDataBean;
import com.roroclaw.base.bean.PageBean;

public class PageControllerAOP {

	private static Logger logger = LoggerFactory.getLogger(PageControllerAOP.class);

	// 方法执行的前后调用
	public Object around(ProceedingJoinPoint point) throws Throwable {
		Object[] objs = point.getArgs();
		// 判断是否pagebean,webrequest参数存在,进行参数组装
		PageBean pageBean = null;
		WebRequest webRequest = null;
//		VUserInfo vUserInfo = null;
		if (objs.length > 0) {
			for (int i = 0; i < objs.length; i++) {
				if (objs[i] instanceof PageBean) {
					pageBean = (PageBean) objs[i];
				} else if (objs[i] instanceof WebRequest) {
					webRequest = (WebRequest) objs[i];
				}
//				else if(objs[i] instanceof VUserInfo){
//					vUserInfo = (VUserInfo)objs[i];
//				}
			}
		}
		if (pageBean != null && webRequest != null) {
			pageBean = getPageBeanByRequest(pageBean, webRequest);
			logger.info("进行分页pagebean封装!");
		}
//		if (pageBean != null && vUserInfo != null) {
//			pageBean = getPageBeanByRequest(pageBean, webRequest);
//			logger.info("封装Pagebean中的queryParam");
//		}
		
		try {
			Object object = point.proceed();
			return object;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
	}

	/**
	 * 通过webrequest获取请求pageBean
	 * 
	 * @param webRequest
	 * @return
	 */
	protected PageBean getPageBeanByRequest(PageBean pageBean,
			WebRequest webRequest) {
		// PageBean pageBean = new PageBean();
		Map queryparam = new HashMap();
		Map properties = webRequest.getParameterMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			queryparam.put(name, value);
		}
		pageBean.setQueryparam(queryparam);
		return pageBean;
	}
}
