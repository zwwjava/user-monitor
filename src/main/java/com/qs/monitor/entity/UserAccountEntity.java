package com.qs.monitor.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import java.time.LocalDateTime;
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
@TableName("user_account")
public class UserAccountEntity extends BaseEntity {

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
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户状态：1启用，2冻结
     */
    private Integer status;

    /**
     * ip地址
     */
    private String ip;

    /**
     * ip地址
     */
    private String ipAddress;

    /**
     * 上次登录时间
     */
    private LocalDateTime lastLoginTime;


}
