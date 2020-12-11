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
 * @since 2020-07-06
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("task_schedule_job")
public class TaskScheduleJobEntity extends BaseEntity {

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
     * 工作任务状态
     */
    private Integer jobStatus;

    /**
     * 工作任务触发配置
     */
    private String cronExpression;

    /**
     * 工作任务描述
     */
    private String description;

    /**
     * 任务执行URL
     */
    private String taskUrl;

    /**
     * 是否并发
     */
    private Integer isConcurrent;


}
