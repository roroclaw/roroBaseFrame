package com.roroclaw.base.handler;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.roroclaw.base.bean.InfcDataBean;
import com.roroclaw.base.handler.BizException;
import com.roroclaw.base.utils.Constants;
import com.roroclaw.base.utils.JsonKit;
import org.springframework.web.servlet.view.RedirectView;

public class ExceptionHandler implements HandlerExceptionResolver {
	private static Logger logger = LoggerFactory
			.getLogger(ExceptionHandler.class);

	private String loginUrl ;

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object obj, Exception exception) {
		ModelAndView mv = null;
		boolean isHtmlReq = Constants.isHtmlReq(request);
		Map exMap = new HashMap();
		exception.printStackTrace();
		logger.error(exception.getMessage());
		if (exception instanceof BizException) {
			BizException bizException = (BizException) exception;
			String errorMes = exception.getMessage();
			exMap.put("object", null);
			if (bizException.getStatusCode() != null) {
				exMap.put("status", bizException.getStatusCode());
			} else {
				exMap.put("status", Constants.EXCEPTION_CODE.STATUS_FAIL);
			}
			exMap.put("describe", errorMes);
		} else {
			exMap.put("object", null);
			exMap.put("status", Constants.EXCEPTION_CODE.STATUS_FAIL);
			exMap.put("describe", Constants.EXCEPTION_MSG.RUNTIME_EXCEPTION);
		}

		try {
			if (isHtmlReq) {
				String contextpath;
				contextpath = request.getContextPath();
				request.setAttribute("contextpath", contextpath);
				request.setAttribute("exMap", exMap);
				if(this.loginUrl == null || "".equals(this.loginUrl)){
					this.loginUrl = "/login.html";
				}
				if(Constants.EXCEPTION_CODE.STATUS_FAIL_USER_VALIDATE.equals(exMap.get("status"))){//如果是非法用户,自动跳转登陆界面
					mv = new ModelAndView("redirect:/"+this.loginUrl);
				}else{
					mv = new ModelAndView("error");
				}

			} else {
				response.setHeader("Content-type","application/json;charset=UTF-8");
				response.setCharacterEncoding("UTF-8");
				PrintWriter writer = response.getWriter();
				ObjectMapper mapper = new ObjectMapper();
				String json = mapper.writeValueAsString(exMap);
				writer.write(json);
				writer.flush();
				writer.close();
			}
		} catch (Exception e) {
//			e.printStackTrace();
		}finally {
			return mv;
		}
	}

	public String getLoginUrl() {
		return loginUrl;
	}

	public void setLoginUrl(String loginUrl) {
		this.loginUrl = loginUrl;
	}

}
