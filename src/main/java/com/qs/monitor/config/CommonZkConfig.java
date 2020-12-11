package com.qs.monitor.config;

import com.alibaba.fastjson.JSONObject;
import com.qskj.zookeeperconfig.annotation.ZkBean;
import com.qskj.zookeeperconfig.annotation.ZkValue;
import lombok.Data;

/**
 * @author zhaww
 * @date 2020/8/19
 * @Description .zookeeper 配置信息
 */
@Data
@ZkBean
public class CommonZkConfig {

    /**
     * ---------------------------------------账户准入配置---------------------------------
     */
    /**
     * 校验账号密码次数
     */
    @ZkValue("assess/limitErrorPassword")
    private Integer limitErrorPassword;

    /**
     * 校验验证码次数
     */
    @ZkValue("assess/limitAllopatric")
    private Integer limitAllopatric;

    /**
     * ---------------------------------------应用方配置---------------------------------
     */
    /**
     * 应用方配置
     */
    @ZkValue("application/appConfig")
    private String appConfig;


    /**
     * ----------------------------------------test----------------------------------
     */
    /**
     * 免费送会员推送url
     */
    @ZkValue("system/test")
    private String test;








    /**
     * ----------------------------------------后续使用----------------------------------
     */
    public String queryAppName(String appName) {
        JSONObject jsonObject = JSONObject.parseObject(appConfig);
        return jsonObject.getString(appName);
    }

}
