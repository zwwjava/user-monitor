package com.qs.monitor.entity;

import com.qs.monitor.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 定时任务调度
 * </p>
 *
 * @author zww
 * @since 2020-09-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("task_process_logs")
public class TaskProcessLogsEntity extends BaseEntity {

    private static final long serialVersionUID=1L;

    /**
     * 应用名
     */
    private String applicationName;

    /**
     * 工作任务组
     */
    private String jobGroup;

    /**
     * 工作任务名称
     */
    private String jobName;

    /**
     * 调用结果(0-否，1-是)
     */
    private Boolean sucessFlag;

    /**
     * 调用结果(0-否，1-是)
     */
    private String resultData;


}
