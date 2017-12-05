package com.roroclaw.base.handler.interceptor;

import java.net.URL;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * cors跨域处理
 * 
 * @author dxzadmin
 * 
 */
public class CORSInterceptor extends HandlerInterceptorAdapter {
	private static Logger logger = LoggerFactory
			.getLogger(CORSInterceptor.class);

	private List<String> defaultAccessAllowedFrom;

	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String referer = request.getHeader("Referer");
		String origin = request.getHeader("Origin");
		String method = request.getMethod();
		if (referer != null) {//无头文件说明,不允许跨域
			URL u = new URL(referer);
			String host = u.getHost().toLowerCase();

			if (defaultAccessAllowedFrom != null) {
				for (String s : defaultAccessAllowedFrom) {
					if (host.matches(s)) {
						response.setHeader("Access-Control-Allow-Origin",
								origin);
						break;
					}
				}
			} else {
				response.setHeader("Access-Control-Allow-Origin", origin);
			}
			response.setHeader("Access-Control-Allow-Headers", "Content-Type");
			response.setHeader("Access-Control-Allow-Methods", "POST,GET");
			response.setHeader("Allow", "POST,GET");
		}
		if("OPTIONS".equals(method.toUpperCase())){//屏蔽跨域预检请求
			return false;
		}
		return true;
	}

	public List<String> getDefaultAccessAllowedFrom() {
		return defaultAccessAllowedFrom;
	}

	public void setDefaultAccessAllowedFrom(
			List<String> defaultAccessAllowedFrom) {
		this.defaultAccessAllowedFrom = defaultAccessAllowedFrom;
	}

}
