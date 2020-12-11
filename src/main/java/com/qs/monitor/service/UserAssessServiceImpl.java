package com.qs.monitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qs.monitor.component.Auth;
import com.qs.monitor.config.CommonZkConfig;
import com.qs.monitor.entity.UserAccountEntity;
import com.qs.monitor.entity.UserLoginLogsEntity;
import com.qs.monitor.enums.assess.AssessSystemEnums;
import com.qs.monitor.enums.assess.LoginLogStauts;
import com.qs.monitor.enums.assess.UserAccountStauts;
import com.qs.monitor.enums.common.ErrorCodeEnums;
import com.qs.monitor.exception.CommonException;
import com.qs.monitor.utils.common.CommonThreadPool;
import com.qs.monitor.utils.common.CommonUtils;
import com.qs.monitor.utils.ZhongLianSms;
import com.qs.monitor.utils.common.MyBeanUtils;
import com.qs.monitor.vo.userAssess.UserLoginAssessResponseVo;
import com.qs.monitor.vo.userAssess.UserLoginAssessVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaww
 * @date 2020/9/24
 * @Description .
 */
@Service
@Slf4j
public class UserAssessServiceImpl implements UserAssessService {

    private static final String IP_BLACK_LIST = "ASSESS_IP_BLACK_LIST_";

    private static final String ERROR_ACCOUNT_PASSWORD = "ERROR_ACCOUNT_PASSWORD_";

    private static final String ALLOPATRIC_IP = "ALLOPATRIC_IP_";

    private static final String LOGIN_SMS_VERIFY_CODE = "LOGIN_SMS_VERIFY_CODE_";

    @Resource
    protected Auth auth;

    @Resource
    private UtilsService utilsService;

    @Resource
    private IUserAccountService userAccountService;

    @Resource
    private IUserLoginLogsService userLoginLogsService;

    @Resource
    private CommonZkConfig commonZkConfig;

    @Resource
    private RedisTemplate<String, String> redisTemplate;

    /**
     * 用户登录风险控制
     * @param userLoginAssessVo
     * @return
     */
    @Override
    public UserLoginAssessResponseVo loginAssess(UserLoginAssessVo userLoginAssessVo) {
        log.info("用户登录风险控制");
        UserLoginAssessResponseVo result = new UserLoginAssessResponseVo();
        UserLoginLogsEntity userLoginLogsEntity = new UserLoginLogsEntity();
        Integer actionCode = 1;
        try {
            log.info("系统秘钥检查");
            //初始化日志
            initUserLoginLogsEntity(userLoginLogsEntity, userLoginAssessVo);
            checkSystemSecret(userLoginAssessVo);

            //ip地址
            String address = utilsService.ipQuery(userLoginAssessVo.getIp()).getAddress();
            userLoginLogsEntity.setIpAddress(address);

            log.info("IP黑名单检查");
            checkIPBlackList(userLoginAssessVo);

            log.info("账号密码检查-账号密码错误次数统计");
            actionCode = 2;
            UserAccountEntity userAccountEntity = checkUserAccount(userLoginAssessVo, address);
            //设置返回数据
            setUserLoginAssessResponseVo(result,userAccountEntity, userLoginAssessVo.getIp());

            log.info("IP异地检查-验证码检查-发送验证码");
            actionCode = 3;
            Boolean allopatric = checkAllopatricIP(userLoginAssessVo, userAccountEntity, address, userLoginLogsEntity);

            log.info("用户：" + userAccountEntity.getPhone() + " 成功登录");
            actionCode = 4;
            successLogin(userAccountEntity, userLoginAssessVo.getIp(), address);
            userLoginLogsEntity.setLoginStatus(LoginLogStauts.SUCCESS.getCode());

        } catch (Exception e) {
            throw e;
        } finally {
            userLoginLogsEntity.setActionCode(actionCode);
            //异步保存log
            CommonThreadPool.execute(() -> {
                //保存日志
                userLoginLogsService.save(userLoginLogsEntity);
            });
        }

        return result;
    }

    private void successLogin(UserAccountEntity userAccountEntity, String ip, String address) {
        userAccountEntity.setIp(ip);
        userAccountEntity.setIpAddress(address);
        userAccountEntity.setLastLoginTime(LocalDateTime.now());
        userAccountService.updateById(userAccountEntity);

        //清理该IP，错误密码次数、异地次数
        String errorSize = redisTemplate.opsForValue().get(ERROR_ACCOUNT_PASSWORD + ip);
        String allopatricSize = redisTemplate.opsForValue().get(ALLOPATRIC_IP + ip);
        log.info("登录成功，清理该IP，错误密码次数:" + errorSize + "、异地次数:" + allopatricSize);
        Set<String> keys = redisTemplate.keys("*_" + ip);
        redisTemplate.delete(keys);
    }

    private void setUserLoginAssessResponseVo(UserLoginAssessResponseVo result, UserAccountEntity userAccountEntity, String ip) {
        log.info("设置返回数据");
        result.setLastLoginTime(userAccountEntity.getLastLoginTime());
        result.setAccountId(userAccountEntity.getId());
        result.setPhone(userAccountEntity.getPhone());
        result.setLastLoginAddress(userAccountEntity.getIpAddress());
        //上一个异地登录的日志
        QueryWrapper queryWrapper = new QueryWrapper<UserLoginLogsEntity>().eq("system_code", userAccountEntity.getSystemCode())
                .eq("account", userAccountEntity.getAccount())
//                .eq("allopatric_flag", true)
                .last("limit 1")
                .orderByDesc("create_time");
        UserLoginLogsEntity log = userLoginLogsService.getOne(queryWrapper);
        if (log != null && log.getAllopatricFlag() && !log.getIp().equals(ip)) {
            result.setErrorLoginInfo(log);
        }
    }

    private void initUserLoginLogsEntity(UserLoginLogsEntity userLoginLogsEntity, UserLoginAssessVo userLoginAssessVo) {
        MyBeanUtils.copyProperties(userLoginAssessVo, userLoginLogsEntity);
        userLoginLogsEntity.setLoginStatus(LoginLogStauts.FAIL.getCode());
    }

    /**
     * IP异地检查-验证码检查
     * @param userLoginAssessVo
     * @param userAccountEntity
     * @param address
     */
    private Boolean checkAllopatricIP(UserLoginAssessVo userLoginAssessVo, UserAccountEntity userAccountEntity, String address, UserLoginLogsEntity userLoginLogsEntity) {
        if (StringUtils.isEmpty(userAccountEntity.getIp())) {
            return false;
        }
        Boolean allopatric = !userLoginAssessVo.getIp().equals(userAccountEntity.getIp());
        if (allopatric) {
            log.info("IP异地再判断城市-" + address + "，" + userAccountEntity.getIpAddress());
            allopatric = !address.equals(userAccountEntity.getIpAddress());
        }
        log.info("IP异地检查-" + allopatric);
        userLoginLogsEntity.setAllopatricFlag(allopatric);

        if (allopatric) {
            //重发验证码
            if (StringUtils.isNotEmpty(userLoginAssessVo.getResend()) && "true".equals(userLoginAssessVo.getResend())) {
                //发送验证码
                sendVerifyCodeSms(userLoginAssessVo.getSystemCode(), userAccountEntity.getPhone());
                throw new CommonException(ErrorCodeEnums.RESEND_VERIFY_SMS);
            }
            //检查异地ip次数
            checkAllopatricSize(userLoginAssessVo, address);
            String verifyCode = userLoginAssessVo.getVerifyCode();
            if (StringUtils.isEmpty(verifyCode)) {
                //发送验证码
                sendVerifyCodeSms(userLoginAssessVo.getSystemCode(), userAccountEntity.getPhone());
                throw new CommonException(ErrorCodeEnums.VERIFY_SMS);
            } else {
                String originVerifyCode = redisTemplate.opsForValue().get(LOGIN_SMS_VERIFY_CODE + userAccountEntity.getPhone());
                log.info("验证码检查-" + originVerifyCode + "，" + verifyCode);
                if (StringUtils.isEmpty(originVerifyCode)) {
                    //发送验证码
                    sendVerifyCodeSms(userLoginAssessVo.getSystemCode(), userAccountEntity.getPhone());
                    throw new CommonException(ErrorCodeEnums.VERIFY_SMS_TIMEOUT);
                } else if (!originVerifyCode.equals(verifyCode)){
                    throw new CommonException(ErrorCodeEnums.VERIFY_SMS_ERROR);
                }
            }
        }
        return allopatric;
    }

    private void checkAllopatricSize(UserLoginAssessVo userLoginAssessVo, String address) {
        //查询异地登录次数
        String allopatricSize = redisTemplate.opsForValue().get(ALLOPATRIC_IP + userLoginAssessVo.getIp());
        //异常次数超出限制，禁止IP
        Integer allopatricSizeInt = convertIntValue(allopatricSize);
        log.info("IP异地检查次数统计，IP-" + userLoginAssessVo.getIp() + "，错误次数-" + allopatricSizeInt);
        if (allopatricSizeInt >= commonZkConfig.getLimitAllopatric()) {
            //加入ip黑名单
            redisTemplate.opsForHash().put(IP_BLACK_LIST,userLoginAssessVo.getIp(),address);
            Set<String> keys = redisTemplate.keys("*_" + userLoginAssessVo.getIp());
            redisTemplate.delete(keys);
            throw new CommonException(ErrorCodeEnums.BAN_IP);
        }else {
            if (allopatricSizeInt.equals(1)) {
                redisTemplate.opsForValue().set(ALLOPATRIC_IP + userLoginAssessVo.getIp(), allopatricSizeInt.toString(), 1, TimeUnit.DAYS);
            } else {
                redisTemplate.opsForValue().set(ALLOPATRIC_IP + userLoginAssessVo.getIp(), allopatricSizeInt.toString(), 0);
            }
        }
    }

    private void sendVerifyCodeSms(String systemCode, String phone) {
        log.info("异地短信发送");
        try {
            String content = CommonUtils.getRandomCode();
            ZhongLianSms.send("【登录风控】" + commonZkConfig.queryAppName(systemCode) + "后台异地登录，验证码:" + content + " (手机动态验证码，十分钟内有效)。若非您本人操作，请更新账号密码", phone);
            redisTemplate.opsForValue().set(LOGIN_SMS_VERIFY_CODE + phone, content, 10, TimeUnit.MINUTES);
        } catch (Exception e) {
            log.error("短信发送失败", e);
        }
    }

    private Integer convertIntValue(String allopatricSize) {
        Integer allopatricSizeInt = 1;
        if (StringUtils.isNotEmpty(allopatricSize)) {
            allopatricSizeInt = Integer.parseInt(allopatricSize);
            allopatricSizeInt++;
        }
        return allopatricSizeInt;
    }

    /**
     * 账号密码检查-账号密码错误次数统计
     * @param userLoginAssessVo
     */
    private UserAccountEntity checkUserAccount(UserLoginAssessVo userLoginAssessVo, String address) {
        QueryWrapper queryWrapper = new QueryWrapper<UserAccountEntity>()
                .eq("system_code", userLoginAssessVo.getSystemCode())
                .eq("account", userLoginAssessVo.getAccount())
                .eq("password", CommonUtils.getMD5(userLoginAssessVo.getPassword()));
        UserAccountEntity userAccountEntity = userAccountService.getOne(queryWrapper);
        if (userAccountEntity == null) {
            //检查账号密码错误次数
            checkUserAccountSize(userLoginAssessVo, address);
            throw new CommonException(ErrorCodeEnums.WRONG_PASSWORD);
        } else {
            if (userAccountEntity.getStatus().equals(UserAccountStauts.FROZEN.getCode())) {
                throw new CommonException("账号被冻结！");
            }
        }

        return userAccountEntity;
    }

    /**
     * 检查账号密码错误次数
     * @param userLoginAssessVo
     * @param address
     */
    private void checkUserAccountSize(UserLoginAssessVo userLoginAssessVo, String address) {
        log.info("检查账号密码错误次数：" + userLoginAssessVo.getIp());
        String errorSize = redisTemplate.opsForValue().get(ERROR_ACCOUNT_PASSWORD + userLoginAssessVo.getIp());
        //异常次数
        Integer errorSizeInt = convertIntValue(errorSize);
        log.info("账号密码错误次数统计，IP-" + userLoginAssessVo.getIp() + "，错误次数-" + errorSizeInt);
        if (errorSizeInt >= commonZkConfig.getLimitErrorPassword()) {
            //加入ip黑名单
            redisTemplate.opsForHash().put(IP_BLACK_LIST,userLoginAssessVo.getIp(),address);
            Set<String> keys = redisTemplate.keys("*_" + userLoginAssessVo.getIp());
            redisTemplate.delete(keys);
        } else {
            if (errorSizeInt.equals(1)) {
                redisTemplate.opsForValue().set(ERROR_ACCOUNT_PASSWORD + userLoginAssessVo.getIp(), errorSizeInt.toString(), 1, TimeUnit.DAYS);
            } else {
                redisTemplate.opsForValue().set(ERROR_ACCOUNT_PASSWORD + userLoginAssessVo.getIp(), errorSizeInt.toString(), 0);
            }
        }
    }

    /**
     * IP黑名单检查
     * @param userLoginAssessVo
     */
    private void checkIPBlackList(UserLoginAssessVo userLoginAssessVo) {
        Object ipAddress = redisTemplate.opsForHash().get(IP_BLACK_LIST, userLoginAssessVo.getIp());
        if (ipAddress != null) {
            throw new CommonException(ErrorCodeEnums.BAN_IP);
        }
    }

    /**
     * 系统秘钥检查
     * @param userLoginAssessVo
     */
    private void checkSystemSecret(UserLoginAssessVo userLoginAssessVo) {
        // md5（systemCode_account_password_salt）
        String checkSecret = CommonUtils.getMD5(userLoginAssessVo.getSystemCode() + "_" + userLoginAssessVo.getAccount() + "_" + userLoginAssessVo.getPassword());
        if (!userLoginAssessVo.getSystemSecret().equals(checkSecret)) {
            throw new CommonException(ErrorCodeEnums.ERROR_SECRET);
        }
    }


    @Override
    public void sendLoginSms(UserLoginAssessVo userLoginAssessVo) {
        log.info("重新请求短信验证码");
    }

}
