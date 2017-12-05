package com.roroclaw.base.handler.interceptor;

import com.roroclaw.base.bean.AccTokenBean;
import com.roroclaw.base.service.BaseAuthService;
import com.roroclaw.base.utils.Constants;
import com.roroclaw.base.utils.HttpKit;
import com.roroclaw.base.utils.Springkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;
import java.util.logging.Filter;

/**
 * Created by dengxianzhi on 2016/12/31.
 * 页面跳转权限拦截器,这里做权限的验证
 */
public class AuthInterceptor4Web extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory
            .getLogger(AuthInterceptor4Web.class);

    private List<String> filters;

    private BaseAuthService baseAuthService;

    private String loginUrl ;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean validateBol = true;
        HttpSession session = Springkit.getRequest().getSession();
        //初始化WEB根目录
        String uri = request.getRequestURI();//返回请求行中的资源名称
        String contextpath;
        contextpath = request.getContextPath();
        String url = request.getRequestURL().toString();
        request.setAttribute("contextpath", contextpath);
        //权限验证
        if (this.filterUrl(url) && !uri.equals(contextpath+"/")) {
            String accToken = session.getAttribute(Constants.ACC_TOKEN) != null ? (String) session
                    .getAttribute(Constants.ACC_TOKEN) : "";
            // 验证accToken
            AccTokenBean accTokenBean = new AccTokenBean();
            accTokenBean.setAccToken(accToken);
            accTokenBean.setAccAddress(HttpKit.getIpAddress(request));
            String newtoken = baseAuthService.validateAccToken(accTokenBean);
            if(newtoken != null){
//                session.setAttribute(Constants.ACC_TOKEN, newtoken);
                validateBol = true;
            }else{
                validateBol = false;
            }
        }
        if (validateBol) {
            return super.preHandle(request, response, handler);
        } else {
            if(loginUrl == null || "".equals(loginUrl)){
                loginUrl = "login.html";
            }
            request.getRequestDispatcher("/"+loginUrl).forward(
                    request, response);
            return false;
        }
    }

    /**
     * 过滤
     *
     * @return
     */
    private boolean filterUrl(String url) {
        boolean bol = true;
        int len = this.filters.size();
        for (int i = 0; i < len; i++) {
            String ftag = this.filters.get(i);
            if(url.indexOf(ftag) > -1){
                bol = false;
                break;
            }
        }
        return bol;
    }

    public BaseAuthService getBaseAuthService() {
        return baseAuthService;
    }

    public void setBaseAuthService(BaseAuthService baseAuthService) {
        this.baseAuthService = baseAuthService;
    }

    public List<String> getFilters() {
        return filters;
    }

    public void setFilters(List<String> filters) {
        this.filters = filters;
    }

    public String getLoginUrl() {
        return loginUrl;
    }

    public void setLoginUrl(String loginUrl) {
        this.loginUrl = loginUrl;
    }
}
