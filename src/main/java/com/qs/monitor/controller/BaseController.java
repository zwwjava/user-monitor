package com.qs.monitor.controller;

import com.qs.monitor.common.JsonResult;
import com.qs.monitor.component.Auth;
import com.qs.monitor.enums.common.ErrorCodeEnums;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

/**
 * @author zhaww
 * @date 2020/5/20
 * @Description .
 */
@Slf4j
public class BaseController {

    @Resource
    protected Auth auth;

    protected static <T> JsonResult<T> success() {
        return success(null);
    }

    protected static <T> JsonResult<T> success(T data) {
        return success(data, null);
    }

    protected static <T> JsonResult<T> success(T data, String msg) {
        return buildResp(data, ErrorCodeEnums.SUCCESS, msg);
    }

    private static <T> JsonResult<T> buildResp(T data, ErrorCodeEnums respEnum, String msg) {
        JsonResult<T> resp = new JsonResult<T>();
        resp.setData(data);
        resp.setMsg(respEnum.getMsg());
        resp.setCode(respEnum.getCode().toString());
        return resp;
    }


    /**
     * 检查是否登录在线
     */
    protected String checkLogined() {
        log.info("检查是否登录在线");
        return auth.getUserToken();
    }

}
