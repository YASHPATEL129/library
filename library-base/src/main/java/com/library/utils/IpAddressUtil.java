package com.library.utils;

import com.library.consts.AppConfigs;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.ObjectUtils;

public class IpAddressUtil {

    private static final String LOCAL_IP = "127.0.0.1";

    private static final String LOCAL_REMOTE_HOST = "0:0:0:0:0:0:0:1";

    public static String getIp(HttpServletRequest request){
        if (ObjectUtils.isEmpty(request.getHeader(AppConfigs.REAL_REMOTE_IP_HEADER))){
            return request.getRemoteAddr();
        } else{
            return request.getHeader(AppConfigs.REAL_REMOTE_IP_HEADER);
        }
    }
}
