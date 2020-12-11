package com.qs.monitor.vo.userAssess;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author zhaww
 * @date 2020/9/21
 * @Description .
 */
@Data
public class UserAddVo {

    /**
     * 三方系统编码
     */
    @NotBlank(message = "三方系统编码不能为空")
    private String systemCode;

    /**
     * 账号
     */
    @NotBlank(message = "账号不能为空")
    private String account;

    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空")
    private String password;

    /**
     * 手机号
     */
    @NotBlank(message = "手机号不能为空")
    private String phone;

    /**
     * 秘钥
     */
    private String secret;

}
