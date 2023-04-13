package com.yhm.universityhelper.util;

import com.alibaba.druid.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Component
public class IpUtils {
    private static final AtomicReference<Searcher> SEARCHER = new AtomicReference<>();

    private static final String[] CITY_INFO_COLUMNS = {"country", "province", "city"};

    public static String getIpAddr(HttpServletRequest request) {
        String ip = null;
        try {
            ip = request.getHeader("x-forwarded-for");
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_CLIENT_IP");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getHeader("HTTP_X_FORWARDED_FOR");
            }
            if (StringUtils.isEmpty(ip) || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
                ip = request.getRemoteAddr();
                if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip) || "localhost".equals(ip)) {
                    //根据网卡取本机配置的IP
                    InetAddress inet = null;
                    try {
                        inet = InetAddress.getLocalHost();
                    } catch (UnknownHostException e) {
                        log.error("getIpAddress exception:", e);
                    }
                    ip = Objects.requireNonNull(inet).getHostAddress();
                }
            }
        } catch (Exception e) {
            log.error("IPUtils ERROR ", e);
        }
        return ip;
    }

    /**
     * 根据ip从 ip2region.xdb 中获取地理位置
     *
     * @return 地理位置
     */
    public static Map<String, Object> getCityInfo(String ip) {
        //数据格式： 国家|区域|省份|城市|ISP
        //192.168.31.160 0|0|0|内网IP|内网IP
        //47.52.236.180 中国|0|香港|0|阿里云
        //220.248.12.158 中国|0|上海|上海市|联通
        //164.114.53.60 美国|0|华盛顿|0|0
        HashMap<String, Object> cityInfo = new HashMap<>();
        try {
            String searchIpInfo = SEARCHER.get().search(ip);
            String[] splitIpInfo = searchIpInfo.split("\\|");
            cityInfo.put("ip", ip);
            cityInfo.put("searchInfo", searchIpInfo);
            cityInfo.put("country", splitIpInfo[0]);
            cityInfo.put("region", splitIpInfo[1]);
            cityInfo.put("province", splitIpInfo[2]);
            cityInfo.put("city", splitIpInfo[3]);
            cityInfo.put("ISP", splitIpInfo[3]);
            return cityInfo;
        } catch (Exception e) {
            log.info("failed to search(%s): %s\n", ip, e);
        }
        return null;
    }
    
    public static Map<String, Object> getCityInfo(HttpServletRequest request) {
        return getCityInfo(getIpAddr(request));
    }

    public static String getRegion(String ip) {
        Map<String, Object> cityMap = getCityInfo(ip);
        if (cityMap == null) {
            return null;
        }

        if ("内网IP".equals(cityMap.get("city"))) {
            return "内网IP";
        }

        StringBuilder cityInfo = new StringBuilder();
        for (String key : CITY_INFO_COLUMNS) {
            if (cityMap.get(key) != null && !"0".equals(cityMap.get(key))) {
                cityInfo.append(cityMap.get(key));
            }
        }
        return cityInfo.toString();
    }
    
    public static String getRegion(HttpServletRequest request) {
        return getRegion(getIpAddr(request));
    }

    @PostConstruct
    private static void initResource() {
        try {
            InputStream inputStream = new ClassPathResource("/static/ip2region.xdb").getInputStream();
            byte[] dbBinStr = FileCopyUtils.copyToByteArray(inputStream);
            SEARCHER.set(Searcher.newWithBuffer(dbBinStr));
        } catch (Exception e) {
            log.info("failed to create content cached searcher: %s\n", e);
        }
    }
}
