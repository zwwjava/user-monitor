package com.qs.monitor.utils.common;

import com.qs.monitor.enums.common.ErrorCodeEnums;
import com.qs.monitor.exception.CommonException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhaww
 * @date 2020/5/20
 * @Description .通用工具
 */
@Slf4j
public class CommonUtils {

    private static final Pattern CHINA_PATTERN = Pattern.compile("^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$");

    //用于混交md5
    private static final String slat = "&%5123***&&%%$$#@";

    /**
     * 生成md5
     * @param str
     * @return
     */
    public static String getMD5(String str) {
        String base = str + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    /**
     * 获取UUID
     * @return
     */
    public static String getUuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 手机号码前三后四脱敏
     * @param mobile
     * @return
     */
    public static String mobileEncrypt(String mobile) {
        if (StringUtils.isEmpty(mobile) || (mobile.length() != 11)) {
            return mobile;
        }
        return mobile.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 批量检查必填参数
     * @param args
     */
    public static void argsNotEmpty(Object... args) {
        for (int i = 0; i < args.length; i++) {
            argNotEmpty(args[i], null);
        }
    }

    /**
     * 检查必填参数
     * @param arg
     * @param message
     */
    public static void argNotEmpty(Object arg, String message) {
        boolean isEmpty;
        if (arg instanceof String) {
            isEmpty = StringUtils.isEmpty((String)arg);
        } else {
            isEmpty = arg == null;
        }

        if (isEmpty) {
            if (StringUtils.isEmpty(message)) {
                throw new CommonException(ErrorCodeEnums.PARAM_EMPTY);
            }
            throw new CommonException(message);
        }
    }

    /**
     * 字符串转 元 金额转 分
     * @param price
     * @return
     */
    public static Integer parseMoney(String price) {
        if (StringUtils.isNotEmpty(price)) {
            Float money = Float.parseFloat(price) * 100;
            return Math.round(money);
        }
        return 0;
    }

    /**
     * 计算收益金额
     * @param shareAmount 总金额
     * @param bonus 百分比例 50
     * @return
     */
    public static Integer calculateIncome(Integer shareAmount, Float bonus) {
        log.info("计算收益金额: 总佣金 = " + shareAmount + "，佣金比例 = " + bonus);
        Float amount = shareAmount * bonus / 100;
        return Math.round(amount);
    }

    /**
     * 生成随即4位验证码
     */
    public static String getRandomCode() {
        return (int) ((Math.random() * 9 + 1) * 1000) + "";
    }

    /**
     * 校验手机号
     * @param phone
     * @return
     */
    public static Boolean checkPhone(String phone) {
        Matcher m = CHINA_PATTERN.matcher(phone);
        return m.matches();
    }
}
