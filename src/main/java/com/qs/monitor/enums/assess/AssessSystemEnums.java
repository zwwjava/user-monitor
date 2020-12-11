package com.qs.monitor.enums.assess;

import lombok.Getter;

/**
 * @author zhaww
 * @date 2020/5/7
 * @Description 系统方
 */
@Getter
public enum AssessSystemEnums {
    ZYDD("ZYDD", "展业多多"),
    XDDD("XDDD", "多多"),
    JDF("JDF", "东风"),
    BBDK("BBDK", "白白"),
    DW("DW", "带我"),
    QPYX("QPYX", "轻品优选"),
    XYB("XYB", "信业帮"),
    ;

    //编码
    private String code;

    //介绍
    private String text;

    AssessSystemEnums(String code, String text) {
        this.code = code;
        this.text = text;
    }
}
