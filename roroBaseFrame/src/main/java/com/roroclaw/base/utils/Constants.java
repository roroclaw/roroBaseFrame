package com.roroclaw.base.utils;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

public class Constants {

    /**
     * ************ session值内容常量 **********************
     */
    public static String SESSION_USERID = "session_userid";
    public static final String SESSION_USER_NAME = "session_user_name";
    public static final String ACC_TOKEN = "accToken";

    /**
     * 注解数据类型
     *
     * @author dxz
     */
    public static interface ANNOTATION_DATA_TYPE {
        public String DATA_TYPE_STRING = "string";
        public String DATA_TYPE_INT = "int";
        public String DATA_TYPE_DATE = "date";
        public String DATA_TYPE_DOUBLE = "double";
        public String DATA_TYPE_ARRAY = "array";
    }

    public static interface EXCEPTION_MSG {
        String ERRORUSER = "非法用户,请先登录!";
        String DESCRIBE_SUCCESS = "success";
        String DESCRIBE_FAIL = "fail";
        String RUNTIME_EXCEPTION = "发生错误";
        String STATUS_404 = "资源不存在!";
    }

    public static interface EXCEPTION_CODE {
        String STATUS_SUCCESS = "1"; //调用成功
        String STATUS_FAIL = "0"; //调用失败
        String STATUS_FAIL_USER_VALIDATE = "2"; //用户非法
        String STATUS_OPERATE_VALIDATE = "3"; //操作非法
    }

    public static interface LOG_STATUS {
        int NORMAL = 1;
        int ERROR = 0;
    }

    public static String generatorAccToken() {
        String pid = "";
        String uuid = UUID.randomUUID().toString();
        pid = "T" + uuid.replace("-", "");
        return pid;
    }

    public static final String AJAX_ACCEPT_CONTENT_TYPE = "text/html;type=ajax";

    /**
     * 判断是否html请求
     *
     * @return
     */
    public static boolean isHtmlReq(HttpServletRequest request) {
        boolean bol = true;
        String acceptHeader = request.getHeader("Accept");
        if (acceptHeader.indexOf("application/json") > -1) {
            bol = false;
        } else {
            bol = true;
        }

        return bol;
    }
}
