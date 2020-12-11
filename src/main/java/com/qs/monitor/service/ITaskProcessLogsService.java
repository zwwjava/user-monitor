package com.qs.monitor.service;

import com.qs.monitor.entity.TaskProcessLogsEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qs.monitor.vo.task.ProcessLogsQueryVo;

import java.util.List;

/**
 * <p>
 * 定时任务调度 服务类
 * </p>
 *
 * @author zww
 * @since 2020-09-18
 */
public interface ITaskProcessLogsService extends IService<TaskProcessLogsEntity> {

    List<TaskProcessLogsEntity> queryAll(ProcessLogsQueryVo processLogsQueryVo);
}
