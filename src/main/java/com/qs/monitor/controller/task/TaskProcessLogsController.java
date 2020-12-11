package com.qs.monitor.controller.task;


import com.qs.monitor.common.JsonResult;
import com.qs.monitor.controller.BaseController;
import com.qs.monitor.service.ITaskProcessLogsService;
import com.qs.monitor.vo.task.ProcessLogsQueryVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 定时任务调度 前端控制器
 * </p>
 *
 * @author zww
 * @since 2020-09-18
 */
@RestController
@RequestMapping("/task-process-logs")
public class TaskProcessLogsController extends BaseController {

    @Resource
    private ITaskProcessLogsService taskProcessLogsService;

    /**
     * 查询任务调度日志
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult queryAll(@Valid @RequestBody ProcessLogsQueryVo processLogsQueryVo) {
        checkLogined();
        return success(taskProcessLogsService.queryAll(processLogsQueryVo));
    }

}

