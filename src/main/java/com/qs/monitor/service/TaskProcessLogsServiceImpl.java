package com.qs.monitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qs.monitor.entity.TaskProcessLogsEntity;
import com.qs.monitor.mapper.TaskProcessLogsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qs.monitor.vo.task.ProcessLogsQueryVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 定时任务调度 服务实现类
 * </p>
 *
 * @author zww
 * @since 2020-09-18
 */
@Service
@Slf4j
public class TaskProcessLogsServiceImpl extends ServiceImpl<TaskProcessLogsMapper, TaskProcessLogsEntity> implements ITaskProcessLogsService {

    @Override
    public List<TaskProcessLogsEntity> queryAll(ProcessLogsQueryVo processLogsQueryVo) {
        log.info("查询任务调度日志");
        QueryWrapper queryWrapper = new QueryWrapper<TaskProcessLogsEntity>()
                .eq(StringUtils.isNotEmpty(processLogsQueryVo.getApplicationName()), "application_name", processLogsQueryVo.getApplicationName())
                .eq(StringUtils.isNotEmpty(processLogsQueryVo.getJobGroup()), "job_group", processLogsQueryVo.getJobGroup());
        return baseMapper.selectList(queryWrapper);
    }
}
