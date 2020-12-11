package com.qs.monitor.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qs.monitor.config.CommonZkConfig;
import com.qs.monitor.entity.UserAccountEntity;
import com.qs.monitor.exception.CommonException;
import com.qs.monitor.mapper.UserAccountMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.qs.monitor.utils.common.CommonUtils;
import com.qs.monitor.utils.common.MyBeanUtils;
import com.qs.monitor.vo.userAssess.UserAddVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 * 用户账号信息表 服务实现类
 * </p>
 *
 * @author zww
 * @since 2020-10-22
 */
@Service
@Slf4j
public class UserAccountServiceImpl extends ServiceImpl<UserAccountMapper, UserAccountEntity> implements IUserAccountService {

    @Resource
    private CommonZkConfig commonZkConfig;

    @Override
    public void addAccount(UserAddVo userAddVo) {
        log.info("增加账号");
        if (StringUtils.isEmpty(userAddVo.getSecret()) || !userAddVo.getSecret().equals("8faa741a081d32f37eceba2fe1b2056b")) {
            throw  new CommonException("禁止访问！");
        }
        //检查系统编码
        checkSystemCode(userAddVo.getSystemCode());
        userAddVo.setPassword(CommonUtils.getMD5(CommonUtils.getMD5(userAddVo.getPassword())));
        Boolean inspect = CommonUtils.checkPhone(userAddVo.getPhone());
        if (!inspect) {
            throw  new CommonException("手机号不合法！");
        }
        //账号是否存在
        QueryWrapper queryWrapper = new QueryWrapper<UserAccountEntity>().eq("system_code", userAddVo.getSystemCode())
                .eq("account", userAddVo.getAccount())
                .last("limit 1");
        UserAccountEntity test = baseMapper.selectOne(queryWrapper);
        if (test != null) {
            throw  new CommonException("账号已存在！");
        }
        UserAccountEntity userAccountEntity = new UserAccountEntity();
        MyBeanUtils.copyProperties(userAddVo, userAccountEntity);
        baseMapper.insert(userAccountEntity);

    }

    private void checkSystemCode(String systemCode) {
        String appName = commonZkConfig.queryAppName(systemCode);
        if (StringUtils.isEmpty(appName)) {
            throw  new CommonException("应用方不存在！");
        }
    }
}
