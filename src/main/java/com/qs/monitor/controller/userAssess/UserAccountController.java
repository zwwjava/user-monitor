package com.qs.monitor.controller.userAssess;


import com.qs.monitor.common.JsonResult;
import com.qs.monitor.controller.BaseController;
import com.qs.monitor.service.IUserAccountService;
import com.qs.monitor.vo.userAssess.UserAddVo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * <p>
 * 用户账号信息表 前端控制器
 * </p>
 *
 * @author zww
 * @since 2020-10-22
 */
@RestController
@RequestMapping("/user-account")
public class UserAccountController extends BaseController {

    @Resource
    private IUserAccountService userAccountService;

    /**
     * 增加账号
     * @return
     */
    @RequestMapping(value = "", method = RequestMethod.POST)
    public JsonResult addAccount(@Valid @RequestBody UserAddVo userAddVo) {
        userAccountService.addAccount(userAddVo);
        return success();

    }


}

