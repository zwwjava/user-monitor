package com.qs.monitor.enums.assess;

import lombok.Getter;


/**
 * @author zhaww
 * @date
 * @Description .
 */
@Getter
public enum LoginLogStauts {

    SUCCESS(1,"成功"),
    FAIL(2,"失败"),
    ;


    private Integer code;
    private String text;

    LoginLogStauts(Integer code, String text) {
        this.code = code;
        this.text = text;
    }

}
