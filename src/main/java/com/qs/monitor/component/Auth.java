package com.qs.monitor.component;

import com.alibaba.fastjson.JSONObject;
import com.qs.monitor.common.TokenInfo;
import com.qs.monitor.enums.common.ErrorCodeEnums;
import com.qs.monitor.enums.common.TokenType;
import com.qs.monitor.exception.CommonException;
import com.qs.monitor.utils.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * 用户登录信息组件
 */
@Component
@Slf4j
public class Auth {

    private static String userToken = CommonUtils.getUuid();

    public String getUserToken() {
        String token = getToken();
        log.info("token信息" + token);
        if (StringUtils.isEmpty(token)) {
            throw new CommonException(ErrorCodeEnums.LOGIN_FAILED);
        }
        if (!token.equals(userToken)) {
            throw new CommonException(ErrorCodeEnums.LOGIN_FAILED);
        }
        return userToken;
    }

    public String setUserToken(String userToken) {
        Auth.userToken = userToken;
        return userToken;
    }

    private static final int TIME_OUT_COUNT = 1;
    private static final TimeUnit TIME_OUT_UNIT = TimeUnit.HOURS;

    public static final Function<HttpServletRequest, String> TOKEN_GETTER = req -> {
        String token = req.getHeader("token");
        if (StringUtils.isEmpty(token)) {
            token = req.getParameter("token");
        }
        return token;
    };

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    public String setTokenInfo(Integer userId, TokenType tokenType) {
        String token = tokenType.getTokenPrefix() + userId + "_" + CommonUtils.getUuid();
        TokenInfo tokenInfo = new TokenInfo(userId);
        //退出登录
        removeExist(userId, tokenType);
        delayToken(token, tokenInfo, tokenType);
        return token;
    }

    public String setTokenInfo(Integer userId, TokenType tokenType, String phone) {
        String token = tokenType.getTokenPrefix() + userId + "_" + CommonUtils.getUuid();
        TokenInfo tokenInfo = new TokenInfo(userId, phone);
        //退出登录
        removeExist(userId, tokenType);
        delayToken(token, tokenInfo, tokenType);
        return token;
    }

    public void deleteTokenInfo() {
        String token = getToken();
        if (token != null) {
            redisTemplate.delete(token);
        }
    }

    /**
     * 用户退出
     *
     * @param userId
     */
    public void removeExist(Integer userId, TokenType tokenType) {
        log.info("强制退出登录，userId：" + userId + "，" + tokenType);
        Set<String> keys = redisTemplate.keys(tokenType.getTokenPrefix() + userId + "_*");
        log.info("强制退出登录，数量：" + keys.size());
        redisTemplate.delete(keys);
    }

    /**
     * @param tokenType
     * @param isAnonymity 允许匿名
     * @return
     */
    public TokenInfo getTokenInfo(TokenType tokenType, Boolean isAnonymity) {
        String token = getToken();
        log.info("token信息" + token);
        if (StringUtils.isEmpty(token) && isAnonymity) {
            if (isAnonymity) {
                return null;
            }
            throw new CommonException(ErrorCodeEnums.LOGIN_FAILED);
        }
        String tokenInfoStr = redisTemplate.opsForValue().get(token);
        if (StringUtils.isEmpty(tokenInfoStr)) {
            if (isAnonymity) {
                return null;
            }
            throw new CommonException(ErrorCodeEnums.LOGIN_FAILED);
        }
        delayToken(token, tokenInfoStr, tokenType);
        return JSONObject.parseObject(tokenInfoStr, TokenInfo.class);
    }

    /**
     * 默认不允许匿名登录
     *
     * @param tokenType
     * @return
     */
    public TokenInfo getTokenInfo(TokenType tokenType) {
        return getTokenInfo(tokenType, false);
    }

    private String getToken() {
        try {
            ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            return TOKEN_GETTER.apply(attrs.getRequest());
        } catch (Exception e) {
            log.info("非http请求");
            return null;
        }
    }

    /**
     * 解析token
     */
    public TokenInfo resolveToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return null;
        }
        String tokenInfoStr = redisTemplate.opsForValue().get(token);
        if (StringUtils.isEmpty(tokenInfoStr)) {
            return null;
        }
        return JSONObject.parseObject(tokenInfoStr, TokenInfo.class);
    }

    /**
     * token每次使用时，自动延期
     *
     * @param token
     * @param userInfo
     */
    private void delayToken(String token, TokenInfo userInfo, TokenType tokenType) {
        //token每次使用时，自动延期
        redisTemplate.opsForValue().set(token, JSONObject.toJSONString(userInfo), tokenType.getTokenLifeCount(), tokenType.getTimeUnit());
    }

    private void delayToken(String token, String userInfo, TokenType tokenType) {
        //token每次使用时，自动延期
        redisTemplate.opsForValue().set(token, userInfo, tokenType.getTokenLifeCount(), tokenType.getTimeUnit());
    }

}
