package com.qs.monitor.vo.utils.sms;

import lombok.Data;

/**
 * @Author Zhou
 * @Date$ 2020/5/18 10:58
 **/
@Data
public class HttpClientEntity {
    private String url;
    private String userid;
    private String account;
    private String password;
    private String mobile;
    private String content;
    private String sendTime;
    private String action;
    private String extno;

    private String caller;
    private String smser;

}
