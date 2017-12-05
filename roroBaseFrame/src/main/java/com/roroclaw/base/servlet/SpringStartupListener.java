package com.roroclaw.base.servlet;

import com.roroclaw.base.service.BaseInitService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletContext;

/**
 * Created by dengxianzhi on 2017/1/24.
 */
class SpringStartupListener implements ApplicationContextAware, ServletContextAware,
        InitializingBean, ApplicationListener<ContextRefreshedEvent> {

    private BaseInitService baseInitService;

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

    }

    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

    }

    public void afterPropertiesSet() throws Exception {
        baseInitService.afterPropertiesSet();
    }

    public void setServletContext(ServletContext servletContext) {

    }

    public BaseInitService getBaseInitService() {
        return baseInitService;
    }

    public void setBaseInitService(BaseInitService baseInitService) {
        this.baseInitService = baseInitService;
    }
}
