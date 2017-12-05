package com.roroclaw.base.handler;

import com.roroclaw.base.bean.InfcDataBean;
import com.roroclaw.base.utils.Constants;

/**
 * Created by dengxianzhi on 2017/2/10.
 */
public class AuthorErrorException extends BizException {

    public AuthorErrorException() {
        super(Constants.EXCEPTION_CODE.STATUS_FAIL_USER_VALIDATE,Constants.EXCEPTION_MSG.ERRORUSER);
    }
}
