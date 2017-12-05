package com.roroclaw.base.servlet;

import com.roroclaw.base.bean.InfcDataBean;
import com.roroclaw.base.handler.BizException;
import com.roroclaw.base.utils.Constants;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerExecutionChain;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dengxianzhi on 2017/1/7.
 */
public class CustomDispatcherServlet extends DispatcherServlet {

    @Override
    protected void noHandlerFound(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean isHtmlReq = Constants.isHtmlReq(request);
        Map exMap = new HashMap();
//        exMap.put("status", InfcDataBean.STATUS_FAIL);
        exMap.put("status",Constants.EXCEPTION_CODE.STATUS_FAIL);
        exMap.put("describe",Constants.EXCEPTION_MSG.STATUS_404);
        if (isHtmlReq) {
            String uri = request.getRequestURI();
            String contextpath;
            contextpath = request.getContextPath();
            request.setAttribute("contextpath", contextpath);
            request.setAttribute("exMap", exMap);
//            mv = new ModelAndView("error");
            logger.debug(uri+"不存在!");
            response.sendRedirect(contextpath+"/404.err");
        } else {
            response.setHeader("Content-type",
                    "application/json;charset=UTF-8");
            response.setCharacterEncoding("UTF-8");
            PrintWriter writer = response.getWriter();
            ObjectMapper mapper = new ObjectMapper();
            String json = mapper.writeValueAsString(exMap);
            writer.write(json);
            writer.flush();
            writer.close();
        }
    }
}
