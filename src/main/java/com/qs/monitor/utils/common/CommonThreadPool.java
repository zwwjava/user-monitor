package com.qs.monitor.utils.common;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author zhaww
 * @date 2020/4/20
 * @Description .通用的线程工厂
 */
public class CommonThreadPool {

    /**
     * 线程池执行器
     */
    private static ThreadPoolTaskExecutor COMMON_EXECUTOR = new ThreadPoolTaskExecutor();

    static {
        COMMON_EXECUTOR.setCorePoolSize(10);
        COMMON_EXECUTOR.setMaxPoolSize(20);
        COMMON_EXECUTOR.setQueueCapacity(200);
        COMMON_EXECUTOR.setKeepAliveSeconds(60);
        COMMON_EXECUTOR.setThreadNamePrefix("common-pool-");
        COMMON_EXECUTOR.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        COMMON_EXECUTOR.initialize();
    }

    /**
     * 获得线程池
     * @param
     */
    public static ThreadPoolTaskExecutor getExecutor() {
        return COMMON_EXECUTOR;
    }

    /**
     * 执行
     * @param command
     */
    public static void execute(Runnable command) {
        COMMON_EXECUTOR.execute(command);
    }

}
