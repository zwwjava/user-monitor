package com.qs.monitor.vo.utils;

import lombok.Data;


/**
 * @author zhaww
 * @date 2020/9/21
 * @Description .
 */
@Data
public class IpQueryResponseVo {

    /**
     * 国家
     */
    private String country;

    /**
     * 省
     */
    private String region;

    /**
     * 市
     */
    private String city;

    /**
     * 国-省-市
     */
    private String address;

}
