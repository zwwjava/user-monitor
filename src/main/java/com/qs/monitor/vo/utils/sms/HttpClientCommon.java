package com.qs.monitor.vo.utils.sms;

import com.alibaba.fastjson.JSONObject;
import com.qs.monitor.enums.common.SuccessFail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

/**
 * @Author Zhou
 * @Date$ 2020/5/18 10:54
 **/
@Slf4j
public class HttpClientCommon {

    public static Boolean toPost(HttpClientEntity entity) throws Exception {
        HttpClient client = new HttpClient();
        PostMethod post = new PostMethod(entity.getUrl());
        post.setRequestHeader("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        NameValuePair userid = new NameValuePair("userid", entity.getUserid());
        NameValuePair account = new NameValuePair("account", entity.getAccount());
        NameValuePair password = new NameValuePair("password", entity.getPassword());
        NameValuePair action = new NameValuePair("action", entity.getAction());
        NameValuePair mobile = new NameValuePair("mobile", entity.getMobile());
        NameValuePair text = new NameValuePair("content", entity.getContent());
        NameValuePair sendTime = new NameValuePair("sendTime", "");
        NameValuePair extno = new NameValuePair("extno", "");
        post.setRequestBody(new NameValuePair[]{userid, account, password, mobile, text, sendTime, action, extno});
        int statu = client.executeMethod(post);
        log.info("statu=" + statu);
        String str = post.getResponseBodyAsString();
        return parseResultMsg(str);
    }

    public static Boolean parseResultMsg(String resp) {
        log.info("解析短信结果;" + resp);
        JSONObject result = JSONObject.parseObject(resp);
        if (result != null && result.get("returnstatus").equals("Success")) {
            result.put("commonResult", SuccessFail.SUCCESS.getCode());
            return false;
        } else {
            result.put("commonResult", SuccessFail.FAIL.getCode());
            String msg = result.get("message").toString();
            result.put("commonResultMsg", msg);
        }
        return false;
    }

    //解析xml字符串
    public static JSONObject readStringXml(String xml) {
        JSONObject result = new JSONObject();
        Document doc = null;
        try {
            //将字符转化为XML
            doc = DocumentHelper.parseText(xml);
            //获取根节点
            Element rootElt = doc.getRootElement();

            //获取根节点下的子节点的值
            String returnstatus = rootElt.elementText("returnstatus").trim();
            String message = rootElt.elementText("message").trim();

            if ("Success".equals(returnstatus)) {
                result.put("commonResult", SuccessFail.SUCCESS.getCode());
            } else {
                result.put("commonResult", SuccessFail.FAIL.getCode());
                result.put("commonResultMsg", message);
            }
            log.info("返回状态为：" + returnstatus);
            log.info("返回信息提示：" + message);
            return result;

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return result;
    }

}
