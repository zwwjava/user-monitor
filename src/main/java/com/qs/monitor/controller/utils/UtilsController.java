package com.qs.monitor.controller.utils;

import com.qs.monitor.common.JsonResult;
import com.qs.monitor.controller.BaseController;
import com.qs.monitor.service.UtilsService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author zhaww
 * @date 2020/9/24
 * @Description .用户评估
 */
@RestController
@RequestMapping("/utils")
public class UtilsController extends BaseController {

    @Resource
    private UtilsService utilsService;

    /**
     * 用户登录风险控制
     * @return
     */
    @RequestMapping(value = "/ip/{ip}", method = RequestMethod.GET)
    public JsonResult ipQuery(@PathVariable String ip) {
        return success(utilsService.ipQuery(ip));
    }

}
