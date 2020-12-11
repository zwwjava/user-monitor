package com.qs.monitor.service;

import com.qs.monitor.component.Auth;
import com.qs.monitor.exception.CommonException;
import com.qs.monitor.utils.common.CommonUtils;
import com.qs.monitor.vo.common.UserLoginVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhaww
 * @date 2020/9/24
 * @Description .
 */
@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Resource
    protected Auth auth;

    @Override
    public String login(UserLoginVo userLoginVo) {
        if (userLoginVo.getUsername().equals("admin") && userLoginVo.getPassword().equals("000000") ) {
            String token = auth.setUserToken(CommonUtils.getUuid());
            log.info("用户Token：" + token);
            return token;
        } else {
            throw new CommonException("账户或者密码错误！");
        }
    }
}
