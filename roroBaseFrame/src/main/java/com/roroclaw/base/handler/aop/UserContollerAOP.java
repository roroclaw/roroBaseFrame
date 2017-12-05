package com.roroclaw.base.handler.aop;

import com.roroclaw.base.bean.InfcDataBean;
import com.roroclaw.base.bean.UserBean;
import com.roroclaw.base.handler.AuthorErrorException;
import com.roroclaw.base.handler.BizException;
import com.roroclaw.base.service.BaseAuthService;
import com.roroclaw.base.utils.Constants;
import com.roroclaw.base.utils.HttpKit;
import com.roroclaw.base.utils.Springkit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserContollerAOP {
	private static Logger logger = LoggerFactory
			.getLogger(UserContollerAOP.class);

	private BaseAuthService baseAuthService;

	// 方法执行的前后调用
	public Object around(ProceedingJoinPoint point) throws Throwable {
		InfcDataBean infcDataBean = null;
		Object[] args = point.getArgs();
		Object target = point.getTarget();
		MethodSignature methodSignature = ((MethodSignature) point
				.getSignature());
		String methodName = methodSignature.getName();
		Class[] parameterTypes = methodSignature.getMethod()
				.getParameterTypes();
		HttpServletRequest request = Springkit.getRequest();
//		String accToken = request.getHeader(Constants.ACC_TOKEN);
		HttpSession session = Springkit.getRequest().getSession();
		String accToken = session.getAttribute(Constants.ACC_TOKEN) != null ? (String) session
				.getAttribute(Constants.ACC_TOKEN) : "";
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
		try {
			Object reObject = null;
			UserBean userBean = null;
			for (int i = 0; i < args.length; i++) {
				Object argObj = args[i];
				if (argObj instanceof UserBean) {
					userBean = (UserBean) argObj;
					logger.debug("检查accToken并完善用户信息!");
					userBean = baseAuthService.perfectUserBean(accToken);
					if (userBean == null) {
//						BizException bizException = new BizException(
//								Constants.EXCEPTION_MSG.ERROR);
//						bizException.setStatusCode(InfcDataBean.STATUS_FAIL_USER_VALIDATE);
						logger.debug("检查accToken并完善用户信息错误,请重新登录!");
						throw new AuthorErrorException();
					}
					args[i] = userBean;
				}
			}
			reObject = point.proceed(args);
			return reObject;
		} catch (Exception e) {
			throw e;
		}
	}

	public BaseAuthService getBaseAuthService() {
		return baseAuthService;
	}

	public void setBaseAuthService(BaseAuthService baseAuthService) {
		this.baseAuthService = baseAuthService;
	}
}
