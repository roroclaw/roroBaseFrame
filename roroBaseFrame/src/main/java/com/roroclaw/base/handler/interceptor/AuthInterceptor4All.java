package com.roroclaw.base.handler.interceptor;

import com.roroclaw.base.annotation.Authorize;
import com.roroclaw.base.bean.AccTokenBean;
import com.roroclaw.base.handler.AuthorErrorException;
import com.roroclaw.base.service.BaseAuthService;
import com.roroclaw.base.utils.Constants;
import com.roroclaw.base.utils.HttpKit;
import com.roroclaw.base.utils.Springkit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by roroclaw on 2017/9/23.
 */
public class AuthInterceptor4All extends HandlerInterceptorAdapter {
    private static Logger logger = LoggerFactory
            .getLogger(AuthInterceptor4All.class);

    private List<String> filters;

    private BaseAuthService baseAuthService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean validateBol = false;
//        boolean isHtmlReq = Constants.isHtmlReq(request);
//        String url = request.getRequestURL().toString();

        //过滤权限验证
        boolean isAuth = true;
        if (handler.getClass().isAssignableFrom(HandlerMethod.class)) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Authorize authorize = handlerMethod.getMethodAnnotation(Authorize.class);
            if(authorize != null && !authorize.required()){
                isAuth = false;
                validateBol = true;
            }
        }

        if(isAuth) {
            //初始化WEB根目录
            String uri = request.getRequestURI();//返回请求行中的资源名称
            String contextpath;
            contextpath = request.getContextPath();
            String url = request.getRequestURL().toString();
            request.setAttribute("contextpath", contextpath);
            if(!this.filterUrl(url) || uri.equals(contextpath+"/")){
                isAuth = false;
                validateBol = true;
            }
        }

        if(isAuth){
            //最先从session中取值
            HttpSession session = Springkit.getRequest().getSession();
            String accToken = session.getAttribute(Constants.ACC_TOKEN) != null ? (String) session
                    .getAttribute(Constants.ACC_TOKEN) : "";
//            logger.debug("sessionToken="+accToken);
            if(accToken == null || "".equals(accToken.trim())){
                accToken = request.getAttribute(Constants.ACC_TOKEN) != null ? (String) request
                        .getAttribute(Constants.ACC_TOKEN) : "";
            }
            if(accToken == null || "".equals(accToken.trim())){
                accToken = request.getParameter(Constants.ACC_TOKEN);
            }
            if(accToken == null || "".equals(accToken.trim())){
                accToken = HttpKit.getCookieVal(request, Constants.ACC_TOKEN);
            }

            // 验证accToken
            if(accToken == null || "".equals(accToken.trim())){
                throw new AuthorErrorException();
            }
            AccTokenBean accTokenBean = new AccTokenBean();
            accTokenBean.setAccToken(accToken);
            accTokenBean.setAccAddress(HttpKit.getIpAddress(request));
            String newtoken = baseAuthService.validateAccToken(accTokenBean);
            if(newtoken != null){
                //cookie写入回传newtoken
                validateBol = true;
//                HttpKit.editOrAddCookie(request,response,Constants.ACC_TOKEN,newtoken);
            }
        }

        if (validateBol) {
            return super.preHandle(request, response, handler);
        } else {
            throw new AuthorErrorException();
        }

//        if (validateBol) {
//            return super.preHandle(request, response, handler);
//        } else {
//            if(loginUrl == null || "".equals(loginUrl)){
//                loginUrl = "login.html";
//            }
//            request.getRequestDispatcher("/"+loginUrl).forward(
//                    request, response);
//            return false;
//        }

    }

    /**
     * 过滤
     *
     * @return
     */
    private boolean filterUrl(String url) {
        boolean bol = true;
        if (this.filters != null) {
            int len = this.filters.size();
            for (int i = 0; i < len; i++) {
                String ftag = this.filters.get(i);
                if (url.indexOf(ftag) > -1) {
                    bol = false;
                    break;
                }
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

}
