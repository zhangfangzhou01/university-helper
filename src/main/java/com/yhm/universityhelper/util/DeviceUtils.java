package com.yhm.universityhelper.util;

import cn.hutool.http.useragent.Platform;
import cn.hutool.http.useragent.UserAgentUtil;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class DeviceUtils {
    public static String getPlatformName(HttpServletRequest request) {
        return UserAgentUtil.parse(request.getHeader("User-Agent")).getPlatform().getName();
    }
    
    public static Platform getPlatform(HttpServletRequest request) {
        return UserAgentUtil.parse(request.getHeader("User-Agent")).getPlatform();
    }
    
    public static boolean isMobile(HttpServletRequest request) {
        return UserAgentUtil.parse(request.getHeader("User-Agent")).getPlatform().isMobile();
    }
}
