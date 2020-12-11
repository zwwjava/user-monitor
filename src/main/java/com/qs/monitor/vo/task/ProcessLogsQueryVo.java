package com.qs.monitor.vo.task;

import lombok.Data;

/**
 * @author zhaww
 * @date 2020/9/21
 * @Description .
 */
@Data
public class ProcessLogsQueryVo {

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 工作任务组
     */
    private String jobGroup;

}
