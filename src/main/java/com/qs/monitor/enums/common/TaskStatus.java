package com.qs.monitor.enums.common;

/**
 * Created by ppp on 17-10-20.
 */
public enum TaskStatus {

    ON("ON", 1),
    OFF("OFF", 0);

    private String name;
    private Integer code;

    TaskStatus(String name, Integer code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

}
