package com.qs.monitor.service;

import com.qs.monitor.entity.UserAccountEntity;
import com.baomidou.mybatisplus.extension.service.IService;
import com.qs.monitor.vo.userAssess.UserAddVo;

/**
 * <p>
 * 用户账号信息表 服务类
 * </p>
 *
 * @author zww
 * @since 2020-10-22
 */
public interface IUserAccountService extends IService<UserAccountEntity> {

    void addAccount(UserAddVo userAddVo);
}
