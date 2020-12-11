package com.qs.monitor.vo.userAssess;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhaww
 * @date 2020/9/21
 * @Description .
 */
@Data
public class UserLoginAssessVo {

    /**
     * 系统编码
     */
    @NotBlank(message = "系统编码不能为空")
    private String systemCode;

    /**
     * 用户名
     */
    @NotBlank(message = "用户账号不能为空")
    private String account;

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")
    private String password;

    /**
     * ipAddress地址
     */
    @NotBlank(message = "ipAddress不能为空")
    private String ip;

    /**
     * 系统秘钥 md5（systemCode_account_password_salt）
     */
    @NotBlank(message = "系统秘钥不能为空")
    private String systemSecret;

    /**
     * 验证码
     */
    private String verifyCode;

    /**
     * 重发验证码
     */
    private String resend;

}
