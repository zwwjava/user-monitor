package com.qs.monitor.service;

import com.qs.monitor.entity.TaskScheduleJobEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qs.monitor.vo.task.JobStatusVo;

import java.util.List;

/**
 * <p>
 * 定时任务调度 服务类
 * </p>
 *
 * @author zww
 * @since 2020-07-06
 */
public interface ITaskScheduleJobService extends IService<TaskScheduleJobEntity> {

    /**
     * 获取所有任务
     * @return
     */
    List<TaskScheduleJobEntity> queryAll();

    /**
     * 增加任务
     * @param taskScheduleJobDomain
     * @return
     */
    void create(TaskScheduleJobEntity taskScheduleJobDomain);

    /**
     * 更新任务
     * @param taskScheduleJobDomain
     * @return
     */
    void update(TaskScheduleJobEntity taskScheduleJobDomain);

    /**
     * 删除任务
     * @param id
     * @return
     */
    void delete(Integer id);

    /**
     * 更新Schedule
     * @param id
     * @param code
     */
    void updateSchedule(Integer id, String code);

    /**
     * 查询job运行状态
     * @return
     */
    List<JobStatusVo> queryJobStatus();

    /**
     * 触发任务
     * @param id
     */
    void fire(Integer id);

}
