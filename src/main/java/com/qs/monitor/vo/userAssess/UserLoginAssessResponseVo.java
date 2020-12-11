package com.qs.monitor.vo.userAssess;

import com.qs.monitor.entity.UserLoginLogsEntity;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhaww
 * @date 2020/9/21
 * @Description .
 */
@Data
public class UserLoginAssessResponseVo {

    /**
     * 上次登录地址
     */
    private String lastLoginAddress;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 异常登录信息
     */
    private UserLoginLogsEntity errorLoginInfo;

    /**
     * 用户id
     */
    private Integer accountId;

    /**
     * 用户手机号
     */
    private String phone;

}
