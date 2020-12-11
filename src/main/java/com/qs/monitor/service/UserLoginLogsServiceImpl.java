package com.qs.monitor.service;

import com.qs.monitor.entity.UserLoginLogsEntity;
import com.qs.monitor.mapper.UserLoginLogsMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户账号信息表 服务实现类
 * </p>
 *
 * @author zww
 * @since 2020-10-22
 */
@Service
public class UserLoginLogsServiceImpl extends ServiceImpl<UserLoginLogsMapper, UserLoginLogsEntity> implements IUserLoginLogsService {

}
