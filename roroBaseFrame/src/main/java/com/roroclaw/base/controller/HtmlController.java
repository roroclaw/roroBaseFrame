package com.roroclaw.base.controller;

import com.roroclaw.base.service.BaseAuthService;
import com.roroclaw.base.service.BaseService;
import com.roroclaw.base.utils.Constants;
import com.roroclaw.base.utils.Springkit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Created by dengxianzhi on 2016/12/31.
 */
public class HtmlController extends BaseController {

//	private BaseAuthService baseAuthService;

	@RequestMapping("/{path}.html")
	public ModelAndView routeHtml(@PathVariable String path, WebRequest request)
			throws Exception {
//		HttpSession session = Springkit.getRequest().getSession();
		ModelAndView mv = new ModelAndView();
		mv.addAllObjects(super.getMapByRequest(request));
//		//初始化WEB根目录
//		String contextpath;
//		contextpath = request.getContextPath();
//		mv.addObject("contextpath", contextpath);

//		String tarPath = "login";
//		if (!"login".equals(path)) {
//            String accToken = session.getAttribute(Constants.ACC_TOKEN) != null ? (String) session
//                    .getAttribute(Constants.ACC_TOKEN) : "";
//            // 验证accToken
//            boolean bol = baseAuthService.validateAccToken(accToken);
//            if (bol) {
//                tarPath = path;
//            } else {
//                // 如果session中存在userId,表示在操作,自动刷新token
//                String userId = session.getAttribute(Constants.SESSION_USERID) != null ? (String) session
//                        .getAttribute(Constants.SESSION_USERID) : null;
//                if (userId != null) {
//                    accToken = Constants.generatorAccToken();
//                    this.baseAuthService.refreshToken(userId, accToken);
//                    session.setAttribute(Constants.ACC_TOKEN, accToken);
//                    tarPath = path;
//                }else{
//                	mv.clear();
//                	tarPath = "redirect:/login.html"; //如果鉴权失败，那么重定向到login.jsp
//                }
//
//            }
//		}
//
		mv.setViewName(path);
		return mv;
	}

	@RequestMapping("/*/**/*.html")
	public ModelAndView routeHtml(WebRequest webRequest, HttpServletRequest servletRequest)
			throws Exception {
		String path = servletRequest.getServletPath().replaceFirst("/","").replace(".html","");
		return this.routeHtml(path, webRequest);
	}

//	public BaseAuthService getBaseAuthService() {
//		return baseAuthService;
//	}
//
//	public void setBaseAuthService(BaseAuthService baseAuthService) {
//		this.baseAuthService = baseAuthService;
//	}
}
