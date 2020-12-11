package com.qs.monitor.quartz;

import com.qs.monitor.component.ApplicationContextProvider;
import com.qs.monitor.entity.TaskProcessLogsEntity;
import com.qs.monitor.service.ITaskProcessLogsService;
import com.qs.monitor.vo.task.JobStatusVo;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ppp on 17-10-24.
 */
@Slf4j
@Component
public class FideJobListener implements JobListener {

    private ITaskProcessLogsService taskProcessLogsService;
    public ITaskProcessLogsService getTaskProcessLogsService() {
        if (this.taskProcessLogsService == null) {
            this.taskProcessLogsService = ApplicationContextProvider.getBean(ITaskProcessLogsService.class);
        }
        return this.taskProcessLogsService;
    }

    private static Map<JobKey, JobStatusVo> statusMap = new HashMap<>();

    public static List<JobStatusVo> queryJobStatus() {
        return new ArrayList<>(statusMap.values());
    }

    @Override
    public String getName() {
        return "FideJobListener";
    }

    @Override
    public void jobToBeExecuted(JobExecutionContext context) {
        JobKey jobKey = context.getJobDetail().getKey();
        JobStatusVo statusDto = statusMap.get(jobKey);
        if (statusDto == null) {
            statusDto = new JobStatusVo();
            statusDto.setGroup(jobKey.getGroup());
            statusDto.setName(jobKey.getName());
            statusDto.setFirstFireTime(context.getFireTime());
            statusMap.put(jobKey, statusDto);
        }
        statusDto.setExecuting(true);
        statusDto.setExeCount(statusDto.getExeCount() + 1);
        statusDto.setFireTime(context.getFireTime());
        statusDto.setNextFireTime(context.getNextFireTime());
    }

    @Override
    public void jobExecutionVetoed(JobExecutionContext context) {

    }

    @Override
    public void jobWasExecuted(JobExecutionContext context, JobExecutionException jobException) {
        TaskProcessLogsEntity taskProcessLogsEntity = new TaskProcessLogsEntity();
        JobKey jobKey = context.getJobDetail().getKey();
        JobDataMap dataMap = context.getJobDetail().getJobDataMap();
        taskProcessLogsEntity.setJobGroup(jobKey.getGroup());
        taskProcessLogsEntity.setJobName(jobKey.getName());
        taskProcessLogsEntity.setApplicationName(dataMap.getString("applicationName"));
        taskProcessLogsEntity.setResultData("执行成功");
        taskProcessLogsEntity.setSucessFlag(true);
        JobStatusVo statusDto = statusMap.get(jobKey);
        statusDto.setExecuting(false);
        statusDto.setLatestRunTime(context.getJobRunTime());
        statusDto.setRunTime(statusDto.getRunTime() + context.getJobRunTime());
        statusDto.setPreviousFireTime(statusDto.getFireTime());
        statusDto.setRunTimeAverage(statusDto.getRunTime() / statusDto.getExeCount());
        statusDto.setNextFireTime(context.getNextFireTime());
        if (jobException != null) {
            taskProcessLogsEntity.setSucessFlag(false);
            statusDto.setErrCount(statusDto.getErrCount() + 1);
            taskProcessLogsEntity.setResultData("任务[" + jobKey + "]执行失败：" + jobException.getMessage());
            log.warn("任务[" + jobKey + "]执行失败", jobException);
        }
        try {
            this.getTaskProcessLogsService().save(taskProcessLogsEntity);
        } catch (Exception e) {
            System.out.println("任务日志保存出错");
        }
    }

}
