package com.qs.monitor.controller.userAssess;

import com.qs.monitor.common.JsonResult;
import com.qs.monitor.controller.BaseController;
import com.qs.monitor.service.UserAssessService;
import com.qs.monitor.vo.userAssess.UserLoginAssessVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author zhaww
 * @date 2020/9/24
 * @Description .用户评估
 */
@RestController
@RequestMapping("/user-assess")
public class UserAssessController extends BaseController {

    @Resource
    private UserAssessService userAssessService;

    /**
     * 用户登录风险控制
     * @return
     */
    @RequestMapping(value = "loginAssess", method = RequestMethod.POST)
    public JsonResult loginAssess(@Valid @RequestBody UserLoginAssessVo userLoginAssessVo) {
        return success(userAssessService.loginAssess(userLoginAssessVo));
    }

    /**
     * 请求短信验证码
     * @return
     */
    @RequestMapping(value = "sendLoginSms", method = RequestMethod.POST)
    public JsonResult sendLoginSms(@Valid @RequestBody UserLoginAssessVo userLoginAssessVo) {
        userAssessService.sendLoginSms(userLoginAssessVo);
        return success();
    }

}
