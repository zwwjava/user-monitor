package com.qs.monitor.enums.assess;

import lombok.Getter;


/**
 * @author zhaww
 * @date
 * @Description .
 */
@Getter
public enum UserAccountStauts {

    ON(1,"启用"),
    FROZEN(2,"冻结"),
    ;


    private Integer code;
    private String text;

    UserAccountStauts(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

}
