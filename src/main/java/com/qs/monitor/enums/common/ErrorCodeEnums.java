package com.qs.monitor.enums.common;

public enum ErrorCodeEnums {
    /**
     *  参数为空
     */
    PARAM_EMPTY(8001, "参数为空！"),

    /**
     * 登录失败
     */
    LOGIN_FAILED(10002, "登录失败！"),

    /**
     * 账号密码有误
     */
    WRONG_PASSWORD(11003, "账号密码有误！"),

    /**
     * 校验短信验证码
     */
    VERIFY_SMS(11004, "异地登录，已发送短信验证码！"),

    /**
     * 短信验证码有误
     */
    VERIFY_SMS_ERROR(11005, "校验短信验证码有误！"),

    /**
     * 短信验证码已失效
     */
    VERIFY_SMS_TIMEOUT(11006, "校验短信验证码已失效，已重新发送验证码！"),

    /**
     * 短信验证码已失效
     */
    RESEND_VERIFY_SMS(11007, "已重新发送验证码！"),

    /**
     * IP多次异常操作
     */
    BAN_IP(11008, "IP多次异常操作，已被加入黑名单，有异议可联系管理员！"),

    /**
     * 签名秘钥错误
     */
    ERROR_SECRET(11009, "签名秘钥错误！"),

    /**
     * 系统异常
     */
    SYSTEM_ERROR(500, "系统异常！"),

    /**
     * 通用异常
     */
    COMMON_ERROR(10001, "通用异常"),

    /**
     * 请求成功
     */
    SUCCESS(10000, "请求成功");

    /**
     * 状态码
     */
    private Integer code;
    /**
     * 异常信息
     */
    private String msg;

    /**
     * 异常枚举信息
     *
     * @param code 状态码
     * @param msg  信息
     */
    ErrorCodeEnums(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 获取状态码
     *
     * @return
     */
    public Integer getCode() {
        return code;
    }

    /**
     * 获取信息
     *
     * @return
     */
    public String getMsg() {
        return msg;
    }


    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static ErrorCodeEnums getCommonError(String errorMsg) {
        ErrorCodeEnums result = ErrorCodeEnums.COMMON_ERROR;
        result.setMsg(errorMsg);
        return result;
    }


    /**
     *重写toString方法在控制台显示自定义异常信息
     * @return
     */
    @Override
    public String toString() {
        return "[异常码:"+this.code+" 异常信息:"+this.msg+"]";
    }
}
