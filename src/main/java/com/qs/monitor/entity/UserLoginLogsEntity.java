package com.qs.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 用户账号信息表
 * </p>
 *
 * @author zww
 * @since 2020-10-22
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("user_login_logs")
public class UserLoginLogsEntity extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 三方系统编码
     */
    private String systemCode;

    /**
     * 账号
     */
    private String account;

    /**
     * 登录状态
     */
    private Integer loginStatus;

    /**
     * ip地址
     */
    private String ip;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 异地标记
     */
    private Boolean allopatricFlag;

    /**
     * 登录行为：1校验账号密码，2校验短信验证码
     */
    private Integer actionCode;


}
