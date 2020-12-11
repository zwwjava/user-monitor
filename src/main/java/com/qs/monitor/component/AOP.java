package com.qs.monitor.component;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author zhaww
 * @date 2020/4/9
 * @Description .AOP通用日志记录、动态数据源分发
 */
@Aspect
@Component
@Slf4j
public class AOP {

    private static final int MAX_ARG_PRINT_LENGTH = 1024 * 10;

    /**
     * 不打印参数的类
     */
    private static final Set<Class> EXCEPT_CLASS = new HashSet<>();

    static {
        EXCEPT_CLASS.add(BindingResult.class);
    }

    @Resource
    private JacksonConfig jacksonConfig;

    @Resource
    private ObjectMapper outputJackson;

    private ObjectMapper _inputJackson = null;

    private ObjectMapper inputJackson() {
        if (_inputJackson == null) {
            _inputJackson = jacksonConfig.createObjectMapper();
            _inputJackson.disable(MapperFeature.USE_ANNOTATIONS);
        }
        return _inputJackson;
    }

    /**
     * Controller层路径
     */
    @Pointcut("within(com.qs.*.controller..*)")
    public void controllerPointcut() {
    }

    /**
     * Service层路径
     */
    @Pointcut("within(com.qs.*.service..*)")
    public void servicePointcut() {
    }

    @Around("controllerPointcut()")
    public Object doControllerLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        return doInputOutputLogging(joinPoint, "C_");
    }

    @Around("servicePointcut()")
    public Object doServiceLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        return doInputOutputLogging(joinPoint, "S_");
    }


    /**
     * 记录通用日志
     * @param joinPoint
     * @param prefix
     * @return
     * @throws Throwable
     */
    private Object doInputOutputLogging(ProceedingJoinPoint joinPoint, String prefix) throws Throwable {
        long now = System.currentTimeMillis();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        String typeName = signature.getDeclaringTypeName();
        String method = signature.getName();
        String[] argsNames = signature.getParameterNames();
        Class[] argsTypes = signature.getParameterTypes();
        Class returnType = signature.getReturnType();
        Object[] args = joinPoint.getArgs();
        List<String> argsList = new ArrayList<>();

        for (int i = 0; i < argsNames.length; i++) {
            if (EXCEPT_CLASS.contains(argsTypes[i])) {
                continue;
            }
            String _argVal = inputJackson().writeValueAsString(args[i]);
            if (_argVal != null) {
                int _argValLen = _argVal.length();
                if (_argValLen > MAX_ARG_PRINT_LENGTH) {
                    _argVal = _argVal.substring(0, MAX_ARG_PRINT_LENGTH) + "...(length: " + _argValLen + ")";
                }
            }
            argsList.add("[" + argsTypes[i].getSimpleName() + "]" + argsNames[i] + "=" + _argVal);
        }
        log.debug("{}.{} - [{}{}] <<< 参数: {{}}", typeName, method, prefix, now, StringUtils.join(argsList, ", "));
        try {
            Object result = joinPoint.proceed();
            log.debug("{}.{} - [{}{}] >>> 成功. 返回结果: [{}]{}", typeName, method, prefix, now, returnType.getSimpleName(), outputJackson.writeValueAsString(result));
            return result;
        } catch (Throwable throwable) {
            log.debug("{}.{} - [{}{}] >>> 失败. {}", typeName, method, prefix, now, throwable.toString());
            throw throwable;
        }
    }



}
