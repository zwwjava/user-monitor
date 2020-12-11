package com.qs.monitor.exception;

import com.qs.monitor.enums.common.ErrorCodeEnums;
import lombok.Getter;

/**
 * @author zhaww
 * @date 2020/4/17
 * @Description .通用的异常
 */
@Getter
public class CommonException extends RuntimeException  {

    private static final long serialVersionUID = 1L;

    private ErrorCodeEnums respCode;

    private Throwable e;

    private String debugger;

    /**
     * 通用异常
     * @param respCode
     */
    public CommonException(ErrorCodeEnums respCode) {
        this.respCode = respCode;
    }

    /**
     * 通用异常
     * @param errorMsg
     */
    public CommonException(String errorMsg) {
        this.respCode = ErrorCodeEnums.getCommonError(errorMsg);
    }
    /**
     * 通用异常
     * @param errorMsg
     */
    public CommonException(String errorMsg, String debugger) {
        this.respCode = ErrorCodeEnums.getCommonError(errorMsg);
        this.debugger = debugger;
    }


    /**
     * 返回异常提示信息：
     */
    @Override
    public String getMessage() {
        return this.respCode.getMsg();
    }

    @Override
    public String toString() {
        return super.toString() + (debugger == null ? "" : ": " + debugger);
    }

}
