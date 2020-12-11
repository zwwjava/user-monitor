package com.qs.monitor.quartz;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.annotation.JSONField;
import org.joda.time.DateTime;

import java.util.Date;

/**
 * Created by ppp on 18-5-17.
 */
public class JobRedisObj {

    public static final String REQUEST_HEADER_KEY = "_JOB_REDIS_KEY_";

    public static final String REDIS_KEY_PREFIX = "QS_TOOL_JOB_";

    public String getRedisKey() {
        return REDIS_KEY_PREFIX + this.key;
    }

    @JSONField(serialize = false)
    public DateTime getFireDateTime() {
        return new DateTime(this.fireTime);
    }

    public static JobRedisObj parse(String jsonStr) {
        if (jsonStr == null) {
            return null;
        } else {
            return JSONObject.parseObject(jsonStr, JobRedisObj.class);
        }
    }

    @Override
    public String toString() {
        return "JobRedisObj {" +
                "fireTime=" + fireTime +
                ", key='" + key + '\'' +
                ", desc='" + desc + '\'' +
                ", url='" + url + '\'' +
                ", errorInfo='" + errorInfo + '\'' +
                '}';
    }

    public String toLiteString() {
        return "fireTime=" + getFireDateTime().toString("yyyyMMdd HH:mm:ss") + ", key=" + getRedisKey() + ", errorInfo=" + errorInfo + ", desc=" + desc;
    }

    public JobRedisObj() {
    }

    public JobRedisObj(String key, String desc, Date fireTime, String url) {
        this.key = key;
        this.desc = desc;
        this.fireTime = fireTime;
        this.url = url;
    }

    private String key;

    private String desc;

    private Date fireTime;

    private String url;

    private String errorInfo;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Date getFireTime() {
        return fireTime;
    }

    public void setFireTime(Date fireTime) {
        this.fireTime = fireTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getErrorInfo() {
        return errorInfo;
    }

    public void setErrorInfo(String errorInfo) {
        this.errorInfo = errorInfo;
    }

}
