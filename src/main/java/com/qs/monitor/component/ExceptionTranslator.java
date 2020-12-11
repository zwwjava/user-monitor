package com.qs.monitor.component;

import com.qs.monitor.common.JsonResult;
import com.qs.monitor.enums.common.ErrorCodeEnums;
import com.qs.monitor.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author zhaww
 * @date 2020/4/17
 * @Description .异常返回处理
 */
@ControllerAdvice
@Slf4j
public class ExceptionTranslator {

    /**
     * 处理项目自定义的异常
     */
    @ExceptionHandler(CommonException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResult commonException(CommonException e) {
        log.error("", e);
        return new JsonResult(e.getRespCode());
    }

    /**
     * 处理参数 Valid 异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public JsonResult methodArgumentNotValidException(MethodArgumentNotValidException e) {
        return parseBindingResult(e.getBindingResult());
    }

    private JsonResult parseBindingResult(BindingResult result) {
        JsonResult resp = new JsonResult(ErrorCodeEnums.COMMON_ERROR);
        resp.setMsg(
                result
                        .getFieldErrors()
                        .stream()
                        .map(fe -> fe.getField() + "[" + fe.getDefaultMessage() + "]")
                        .reduce((st1, st2) -> st1 + ", " + st2)
                        .orElse("参数错误")
        );
        return resp;
    }

    /**
     * 处理通用异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public JsonResult processOtherException(Exception e) {
        log.error("遇到非预期的错误", e);
        return new JsonResult(ErrorCodeEnums.SYSTEM_ERROR);
    }

}
