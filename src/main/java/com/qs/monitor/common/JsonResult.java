package com.qs.monitor.common;


import com.qs.monitor.enums.common.ErrorCodeEnums;

/**
 * @Author Zhou
 * @Date 2019/11/8
 **/

public class JsonResult<T> {
    private T data;
    private String code;
    private String msg;

    /**
     * 若没有数据返回，默认状态码为 0，提示信息为“操作成功！”
     */
    public JsonResult() {
        this.code = "10000";
        this.msg = "操作成功！";
        this.data = (T) null;
    }

    /**
     * 若没有数据返回，可以返回空数据，人为指定状态码和提示信息
     *
     * @param code
     * @param msg
     */
    public JsonResult(T data, String code, String msg) {
        this.data = data;
        this.code = code;
        this.msg = msg;
    }

    /**
     * 若没有数据返回，可以人为指定状态码和提示信息
     *
     * @param code
     * @param msg
     */
    public JsonResult(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 返回异常枚举
     * @param errorCodeEnums
     */
    public JsonResult(ErrorCodeEnums errorCodeEnums) {
        this.code = errorCodeEnums.getCode().toString();
        this.msg = errorCodeEnums.getMsg();
        this.data = null;
    }


    /**
     * 有数据返回时，状态码为 0，默认提示信息为“操作成功！”
     *
     * @param data
     */
    public JsonResult(T data) {
        this.data = data;
        this.code = "10000";
        this.msg = "操作成功！";
    }

    /**
     * 有数据返回，状态码为 0，人为指定提示信息
     *
     * @param data
     * @param msg
     */
    public JsonResult(T data, String msg) {
        this.code = "10000";
        this.msg = msg;
        this.data = data;
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
