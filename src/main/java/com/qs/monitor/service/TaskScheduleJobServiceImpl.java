package com.qs.monitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qs.monitor.entity.TaskScheduleJobEntity;
import com.qs.monitor.enums.common.TaskStatus;
import com.qs.monitor.exception.CommonException;
import com.qs.monitor.mapper.TaskScheduleJobMapper;
import com.qs.monitor.quartz.FideJobListener;
import com.qs.monitor.quartz.HttpUrlInvokeJob;
import com.qs.monitor.utils.common.CommonUtils;
import com.qs.monitor.vo.task.JobStatusVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * <p>
 * 定时任务调度 服务实现类
 * </p>
 *
 * @author zww
 * @since 2020-07-06
 */
@Service
@Slf4j
public class TaskScheduleJobServiceImpl extends ServiceImpl<TaskScheduleJobMapper, TaskScheduleJobEntity> implements ITaskScheduleJobService {

    private static Scheduler scheduler = null;

    @PostConstruct
    public void init() {
        log.info("定时任务模块初始化");
        if (scheduler == null) {
            SchedulerFactory schedulerFactory = new StdSchedulerFactory();
            try {
                scheduler = schedulerFactory.getScheduler();
                scheduler.getListenerManager().addJobListener(new FideJobListener());
                scheduler.start();
            } catch (SchedulerException e) {
                log.error("定时任务模块初始化失败", e);
            }
            for (TaskScheduleJobEntity task : queryAll()) {
                if (TaskStatus.ON.getCode().equals(task.getJobStatus())) {
                    try {
                        scheduleJob(task);
                    } catch (Exception e) {
                        log.error("任务 [" + task.getJobGroup() + "." + task.getJobName() + "] 初始化失败", e);
                    }
                }
            }
        }
    }

    @Override
    public List<TaskScheduleJobEntity> queryAll() {
        log.info("查询任务调度配置");
        return baseMapper.selectList(null);
    }

    @Override
    public void create(TaskScheduleJobEntity TaskScheduleJobEntity) {
        log.info("创建任务调度");
        CommonUtils.argsNotEmpty(TaskScheduleJobEntity.getJobGroup());
        CommonUtils.argsNotEmpty(TaskScheduleJobEntity.getJobName());
        CommonUtils.argsNotEmpty(TaskScheduleJobEntity.getCronExpression());
        uniqueCheck(TaskScheduleJobEntity.getJobGroup(), TaskScheduleJobEntity.getJobName(), TaskScheduleJobEntity.getId());
        TaskScheduleJobEntity.setJobStatus(TaskStatus.OFF.getCode());
        baseMapper.insert(TaskScheduleJobEntity);
    }

    @Override
    public void update(TaskScheduleJobEntity TaskScheduleJobEntity) {
        log.info("更新任务调度");
        CommonUtils.argsNotEmpty(TaskScheduleJobEntity.getId());
        offCheck(TaskScheduleJobEntity.getId());
        uniqueCheck(TaskScheduleJobEntity.getJobGroup(), TaskScheduleJobEntity.getJobName(), TaskScheduleJobEntity.getId());
        TaskScheduleJobEntity.setJobStatus(TaskStatus.OFF.getCode());
        baseMapper.updateById(TaskScheduleJobEntity);
    }

    @Override
    public void delete(Integer id) {
        log.info("删除任务调度");
        offCheck(id);
        baseMapper.deleteById(id);
    }

    /**
     * key值唯一性校验
     */
    private void uniqueCheck(String group, String name, Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper<TaskScheduleJobEntity>()
                .eq("job_group",group)
                .eq("job_name",name)
                .eq("id",id);
        if (baseMapper.selectCount(queryWrapper) > 0) {
            throw new CommonException("group + name 需唯一");
        }
    }

    /**
     * 停用状态校验
     */
    private void offCheck(Integer id) {
        QueryWrapper queryWrapper = new QueryWrapper<TaskScheduleJobEntity>()
                .eq("job_status",TaskStatus.ON.getCode())
                .eq("id",id);
        if (baseMapper.selectCount(queryWrapper) > 0) {
            throw new CommonException("任务运行中");
        }
    }

    @Override
    public void updateSchedule(Integer id, String code) {
        log.info("任务调度状态变更");
        TaskScheduleJobEntity TaskScheduleJobEntity = baseMapper.selectById(id);
        if (TaskScheduleJobEntity != null) {
            switch (code) {
                case "on":
                    scheduleJob(TaskScheduleJobEntity);
                    TaskScheduleJobEntity.setJobStatus(TaskStatus.ON.getCode());
                    break;
                case "off":
                    unscheduleJob(TaskScheduleJobEntity);
                    TaskScheduleJobEntity.setJobStatus(TaskStatus.OFF.getCode());
                    break;
            }
            baseMapper.updateById(TaskScheduleJobEntity);
        }
    }

    /**
     * 启用
     */
    private void scheduleJob(TaskScheduleJobEntity task) {
        JobDetail job = null;

        if (StringUtils.isNotEmpty(task.getTaskUrl())) {
            job = JobBuilder.newJob(HttpUrlInvokeJob.class)//调用url的job
                    .withIdentity(task.getJobName(), task.getJobGroup())
                    .usingJobData("desc", task.getDescription())
                    .usingJobData("applicationName", task.getApplicationName())
                    .usingJobData("url", task.getTaskUrl())
                    .build();
        }

        if (job == null) {
            throw new CommonException("任务启用失败，配置不完整");
        }
        CronTrigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(task.getJobName(), task.getJobGroup())
                .withSchedule(CronScheduleBuilder.cronSchedule(task.getCronExpression()))
                .forJob(job)
                .build();
        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            throw new CommonException("任务启用失败");
        }
    }

    /**
     * 停用
     */
    private void unscheduleJob(TaskScheduleJobEntity task) {
        try {
            scheduler.unscheduleJob(TriggerKey.triggerKey(task.getJobName(), task.getJobGroup()));
        } catch (SchedulerException e) {
            throw new CommonException("任务停用失败");
        }
    }

    @Override
    public List<JobStatusVo> queryJobStatus() {
        return FideJobListener.queryJobStatus();
    }

    @Override
    public void fire(Integer id) {
        log.info("触发任务调度");
        TaskScheduleJobEntity TaskScheduleJobEntity = baseMapper.selectById(id);
        if (TaskScheduleJobEntity != null) {
            try {
                scheduler.triggerJob(JobKey.jobKey(TaskScheduleJobEntity.getJobName(), TaskScheduleJobEntity.getJobGroup()));
            } catch (SchedulerException e) {
                throw new CommonException("任务触发失败");
            }
        }
    }

}
