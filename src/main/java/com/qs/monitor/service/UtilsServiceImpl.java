package com.qs.monitor.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.qs.monitor.utils.common.HttpsUtils;
import com.qs.monitor.vo.utils.IpQueryResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author zhaww
 * @date 2020/9/24
 * @Description .
 */
@Service
@Slf4j
public class UtilsServiceImpl implements UtilsService {

    @Override
    public IpQueryResponseVo ipQuery(String ip) {
        IpQueryResponseVo result = new IpQueryResponseVo();
        String params = HttpsUtils.initGetParams("ip", ip,
                "accessKey", "alibaba-inc");
        String url = "http://ip.taobao.com/outGetIpInfo?" + params;
        try {
            String resp = HttpsUtils.get(url);
            if (StringUtils.isNotEmpty(resp)) {
                JSONObject json = JSON.parseObject(resp);
                if (json != null) {
                    json = json.getJSONObject("data");
                    result.setCountry(json.getString("country"));
                    result.setRegion(json.getString("region"));
                    result.setCity(json.getString("city"));
                    result.setAddress(json.getString("country") + "-" + json.getString("region") + "-" + json.getString("city"));
                }
            }

        } catch (Exception e) {
            log.error("查询ip地址出错", e);
            result.setAddress("null");
        }
        return result;
    }

}
