package com.qs.monitor.enums.common;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * @author zhaww
 * @date
 * @Description .
 */
@Getter
public enum TokenType {

    MANAGER("manager", "管理员", "AUTH_MANAGER_",1,  TimeUnit.HOURS),
    USER("user", "用户", "AUTH_USER_",7,  TimeUnit.DAYS),
    ;


    private String code;
    private String text;
    private int tokenLifeCount;
    private String tokenPrefix;
    private TimeUnit timeUnit;

    TokenType(String code, String text, String tokenPrefix, int tokenLifeInDay, TimeUnit timeUnit) {
        this.code = code;
        this.text = text;
        this.tokenLifeCount = tokenLifeInDay;
        this.tokenPrefix = tokenPrefix;
        this.timeUnit = timeUnit;
    }

}
