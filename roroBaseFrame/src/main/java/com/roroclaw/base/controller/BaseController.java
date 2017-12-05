package com.roroclaw.base.controller;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.roroclaw.base.handler.AuthorErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.context.request.WebRequest;

import com.roroclaw.base.bean.InfcDataBean;
import com.roroclaw.base.bean.PageBean;
import com.roroclaw.base.handler.BizException;
import com.roroclaw.base.utils.Constants;

/**
 * Created by dengxianzhi on 2016/12/31.
 */
@Controller
public class BaseController {

	protected static Logger logger = LoggerFactory.getLogger(BaseController.class);

	/**
	 * WebRequest 参数转换map
	 * @param webRequest
	 * @return
	 */
	protected Map getMapByRequest(WebRequest webRequest) {
		Map returnMap = new HashMap();
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
			returnMap.put(name, value);
		}
		return returnMap;
	}
	
	/**
	 * 闁俺绻僿ebrequest閼惧嘲褰囩拠閿嬬湴pageBean
	 * @param webRequest
	 * @return
	 */
	protected PageBean getPageBeanByRequest(PageBean pageBean, WebRequest webRequest) {
//		PageBean pageBean = new PageBean();
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
	
	public void validateUserInfo(String userId,String openId) throws BizException{
		if ((userId == null || "".equals(userId))
				&& (openId == null || "".equals(openId))) {
			throw new AuthorErrorException();
		}
	}
	
//	/**
//	 * 获取request
//	 * @return
//	 */
//	public HttpServletRequest getHttpRequest(){
//			return request;
//	}
	
//	/**
//	 * 获取response
//	 * @return
//	 */
//	public HttpServletResponse getHttpResponse(){
//			return response;
//	}
	
	
//	public void validateUserInfo(VUserInfo userInfo) throws BizException{
//		String userId = userInfo.getUserId();
//		String openId = userInfo.getOpenId();
//		if ((userId == null || "".equals(userId))
//				&& (openId == null || "".equals(openId))) {
//			throw new BizException(Constants.VALIDATE_ERROR_MSG.UNLOGIN);
//		}
//	}
}
