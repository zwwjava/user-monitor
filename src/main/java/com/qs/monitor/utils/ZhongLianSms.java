package com.qs.monitor.utils;

import com.qs.monitor.vo.utils.sms.HttpClientCommon;
import com.qs.monitor.vo.utils.sms.HttpClientEntity;

/**
 * @author zhaww
 * @date 2020/10/23
 * @Description .
 */
public class ZhongLianSms {

    private static final String SMS_ACCOUNT = "hzqskj";
    private static final String PASSWORD = "123456";

    public static void main(String[] args) {
        try {
            send("【登录风控】轻品优选后台，验证码:1234 (手机动态验证码，十分钟内有效)。为了您的账号安全，请勿将短信验证码告诉别人，若非您本人操作，请忽略)","13767222921");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Boolean send(String msg, String phone) throws Exception {
        HttpClientEntity entity = new HttpClientEntity();
        initEntity(entity, phone);
        entity.setContent(msg);
        return HttpClientCommon.toPost(entity);
    }

    private static void initEntity(HttpClientEntity entity, String phone) {
        entity.setAccount(SMS_ACCOUNT);
        entity.setAction("");
        entity.setMobile(phone);
        entity.setPassword(PASSWORD);
        entity.setUrl("http://121.201.57.213/smsJson.aspx?action=send");
        entity.setUserid("");
    }
}
