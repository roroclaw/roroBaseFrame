package com.roroclaw.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.roroclaw.base.handler.BizException;
import org.springframework.web.servlet.ModelAndView;

/**
 * err跳转处理
 */
public class ErrMappingController extends  BaseController{

	@RequestMapping("/404.err")
	public ModelAndView unmappedRequest() throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("404");
		return mv;
	}

	@RequestMapping("/error.err")
	public ModelAndView errorRequest() throws Exception {
		ModelAndView mv = new ModelAndView();
		mv.setViewName("error");
		return mv;
	}

}
