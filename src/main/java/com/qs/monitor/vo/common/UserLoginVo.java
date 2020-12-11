package com.qs.monitor.vo.common;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @author zhaww
 * @date 2020/9/21
 * @Description .
 */
@Data
public class UserLoginVo {

    /**
     * 用户密码
     */
    @NotBlank(message = "用户密码不能为空")
    private String password;

    /**
     * 用户名
     */
    @NotBlank(message = "用户账号不能为空")
    private String username;

}
