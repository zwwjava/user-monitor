package com.qs.monitor.controller.task;


import com.qs.monitor.common.JsonResult;
import com.qs.monitor.controller.BaseController;
import com.qs.monitor.entity.TaskScheduleJobEntity;
import com.qs.monitor.service.ITaskScheduleJobService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * <p>
 * 定时任务调度 前端控制器
 * </p>
 *
 * @author zww
 * @since 2020-07-06
 */
@RestController
@RequestMapping("/task-schedule-job")
public class TaskScheduleJobController extends BaseController {

    @Resource
    private ITaskScheduleJobService taskScheduleJobService;

    /**
     * 获取所有任务信息
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.GET)
    public JsonResult queryAll() {
        checkLogined();
        return success(taskScheduleJobService.queryAll());
    }

    /**
     * 增加任务
     * @param taskScheduleJobDomain
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult create(@RequestBody TaskScheduleJobEntity taskScheduleJobDomain) {
        checkLogined();
        taskScheduleJobService.create(taskScheduleJobDomain);
        return success();
    }

    /**
     * 更新任务
     * @param taskScheduleJobDomain
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.PUT)
    public JsonResult update(@RequestBody TaskScheduleJobEntity taskScheduleJobDomain) {
        checkLogined();
        taskScheduleJobService.update(taskScheduleJobDomain);
        return success();
    }

    /**
     * 删除任务
     * @param id
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public JsonResult delete(@PathVariable Integer id) {
        checkLogined();
        taskScheduleJobService.delete(id);
        return success();
    }

    /**
     * 更新Schedule
     * @param id
     * @param code
     * @return
     */
    @RequestMapping(value = "/updateSchedule/{id}/{code}", method = RequestMethod.GET)
    public JsonResult updateSchedule(@PathVariable Integer id, @PathVariable String code) {
        checkLogined();
        taskScheduleJobService.updateSchedule(id, code);
        return success();
    }

    /**
     * 查询job运行状态
     * @return
     */
    @RequestMapping(value = "/queryJobStatus", method = RequestMethod.GET)
    public JsonResult queryJobStatus() {
        checkLogined();
        return success(taskScheduleJobService.queryJobStatus());
    }

    /**
     * 触发任务
     * @param id
     * @return
     */
    @RequestMapping(value = "/fire/{id}", method = RequestMethod.GET)
    public JsonResult fire(@PathVariable Integer id) {
        checkLogined();
        taskScheduleJobService.fire(id);
        return success();
    }
}

