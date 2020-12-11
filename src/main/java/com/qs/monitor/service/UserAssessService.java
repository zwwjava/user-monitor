package com.qs.monitor.service;


import com.qs.monitor.vo.userAssess.UserLoginAssessResponseVo;
import com.qs.monitor.vo.userAssess.UserLoginAssessVo;

/**
 * <p>
 * 用户服务
 * </p>
 *
 * @author zww
 * @since 2020-09-18
 */
public interface UserAssessService {

    UserLoginAssessResponseVo loginAssess(UserLoginAssessVo userLoginAssessVo);

    void sendLoginSms(UserLoginAssessVo userLoginAssessVo);
}
