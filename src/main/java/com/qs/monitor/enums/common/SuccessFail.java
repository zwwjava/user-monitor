package com.qs.monitor.enums.common;

import lombok.Getter;

/**
 * @author zhaww
 * @date 2020/5/7
 * @Description .短信第三方平台
 */
@Getter
public enum SuccessFail {
    SUCCESS("SUCCESS", "成功"),
    FAIL("FAIL", "失败")
    ;

    //编码
    private String code;

    //介绍
    private String text;

    SuccessFail(String code, String text) {
        this.code = code;
        this.text = text;
    }
}
