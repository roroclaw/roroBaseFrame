package com.roroclaw.base.service;

import com.roroclaw.base.bean.AccTokenBean;
import com.roroclaw.base.bean.UserBean;
import com.roroclaw.base.handler.BizException;

/**
 * Created by roroclaw on 2016/12/31.
 */
public abstract class BaseAuthService extends BaseService{
    public abstract UserBean perfectUserBean(String accToken);

    public abstract String validateAccToken(AccTokenBean accTokenBean) throws BizException;

}
