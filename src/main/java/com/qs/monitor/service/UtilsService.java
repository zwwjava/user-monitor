package com.qs.monitor.service;


import com.qs.monitor.vo.utils.IpQueryResponseVo;

/**
 * <p>
 * 用户服务
 * </p>
 *
 * @author zww
 * @since 2020-09-18
 */
public interface UtilsService {

    IpQueryResponseVo ipQuery(String ip);
}
