package com.roroclaw.base.service;

import com.roroclaw.base.handler.BizException;

/**
 * Created by dengxianzhi on 2017/1/24.
 */
public abstract class BaseInitService {

    public abstract void afterPropertiesSet() throws BizException ;
}
