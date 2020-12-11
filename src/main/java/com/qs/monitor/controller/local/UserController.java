package com.qs.monitor.controller.local;

import com.qs.monitor.common.JsonResult;
import com.qs.monitor.controller.BaseController;
import com.qs.monitor.service.UserService;
import com.qs.monitor.vo.common.UserLoginVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author zhaww
 * @date 2020/9/24
 * @Description .
 */
@RestController
@RequestMapping("/task-user")
public class UserController extends BaseController {

    @Resource
    private UserService userService;

    /**
     * 用户登录
     * @return
     */
    @RequestMapping(value = "login", method = RequestMethod.POST)
    public JsonResult login(@Valid @RequestBody UserLoginVo userLoginVo) {
        return success(userService.login(userLoginVo));
    }

}
