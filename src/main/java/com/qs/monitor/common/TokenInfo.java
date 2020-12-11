package com.qs.monitor.common;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @author zhaww
 * @date 2020/5/25
 * @Description .
 */
@Data
public class TokenInfo {

    private Integer userId;

    private LocalDateTime loginTime;

    private String phone;

    public TokenInfo(Integer userId, LocalDateTime loginTime) {
        this.userId = userId;
        this.loginTime = loginTime;
    }

    public TokenInfo(Integer userId) {
        this.userId = userId;
    }

    public TokenInfo(Integer userId, String phone) {
        this.userId = userId;
        this.phone = phone;
    }

    public TokenInfo() {

    }

}
