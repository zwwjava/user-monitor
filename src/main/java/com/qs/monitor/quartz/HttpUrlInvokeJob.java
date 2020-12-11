package com.qs.monitor.quartz;

import com.qs.monitor.component.StaticHelper;
import com.qs.monitor.utils.common.HttpsUtils;
import org.apache.commons.httpclient.ConnectionPoolTimeoutException;
import org.apache.http.message.BasicHeader;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.net.SocketTimeoutException;


/**
 * Created by ppp on 17-10-20.
 */
@DisallowConcurrentExecution
public class HttpUrlInvokeJob implements Job {

    private static Logger logger = LoggerFactory.getLogger(HttpUrlInvokeJob.class);

    private StringRedisTemplate _redisTemplate;

    private StringRedisTemplate getRedisTemplate() {
        if (_redisTemplate == null) {
            _redisTemplate = StaticHelper.getBean(StringRedisTemplate.class, "stringRedisTemplate");
        }
        return _redisTemplate;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDetail jobDetail = context.getJobDetail();
        JobDataMap dataMap = jobDetail.getJobDataMap();
        JobRedisObj jobRedisObj = new JobRedisObj(
                jobDetail.getKey().toString(),
                dataMap.getString("desc"),
                context.getFireTime(),
                dataMap.getString("url")
        );
        try {
            HttpsUtils.get(dataMap.getString("url"), new BasicHeader(JobRedisObj.REQUEST_HEADER_KEY, jobRedisObj.getRedisKey()));
        } catch (Exception e) {
            if (e instanceof SocketTimeoutException) {
                logger.info("http请求等待超时。task: {}[{}], redisKey: {}, url: {}", jobRedisObj.getKey(), jobRedisObj.getDesc(), jobRedisObj.getUrl());
            } else if (e instanceof ConnectionPoolTimeoutException) {
                logger.info("http连接池等待超时。task: {}[{}], redisKey: {}, url: {}", jobRedisObj.getKey(), jobRedisObj.getDesc(), jobRedisObj.getUrl());
            } else {
                jobRedisObj.setErrorInfo(e.getClass().getName() + ": " + e.getMessage());
                throw new JobExecutionException(e);
            }
        }
    }

//    @Override
//    public void execute(JobExecutionContext context) throws JobExecutionException {
//        StringRedisTemplate _redisTemplate = getRedisTemplate();
//        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
//        JobRedisObj jobRedisObj = new JobRedisObj(
//                context.getJobDetail().getKey().toString(),
//                dataMap.getString("desc"),
//                context.getFireTime(),
//                dataMap.getString("url")
//        );
//        String redisKey = jobRedisObj.getRedisKey();
//        if (!_redisTemplate.hasKey(redisKey)) {
//            _redisTemplate.opsForValue().set(jobRedisObj.getRedisKey(), JSONObject.toJSONString(jobRedisObj));
//            try {
//                HttpsUtils.get(dataMap.getString("url"), new BasicHeader(JobRedisObj.REQUEST_HEADER_KEY, jobRedisObj.getRedisKey()));
//            } catch (Exception e) {
//                if (e instanceof SocketTimeoutException) {
//                    logger.info("http请求等待超时。task: {}[{}], redisKey: {}, url: {}", jobRedisObj.getKey(), jobRedisObj.getDesc(), redisKey, jobRedisObj.getUrl());
//                } else if (e instanceof ConnectionPoolTimeoutException) {
//                    logger.info("http连接池等待超时。task: {}[{}], redisKey: {}, url: {}", jobRedisObj.getKey(), jobRedisObj.getDesc(), redisKey, jobRedisObj.getUrl());
//                } else {
//                    jobRedisObj.setErrorInfo(e.getClass().getName() + ": " + e.getMessage());
//                    _redisTemplate.opsForValue().set(jobRedisObj.getRedisKey(), JSONObject.toJSONString(jobRedisObj));
//                    throw new JobExecutionException(e);
//                }
//            }
//        } else {
//            logger.info("前次任务未结束，跳过本次执行。task: {}[{}], redisKey: {}, url: {}, preJobData: {}", jobRedisObj.getKey(), jobRedisObj.getDesc(), redisKey, jobRedisObj.getUrl(), JobRedisObj.parse(_redisTemplate.opsForValue().get(redisKey)));
//        }
//    }

}
