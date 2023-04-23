package com.yhm.universityhelper.util;

import org.springframework.context.ApplicationContext;

// 一个绕开spring获得bean的工具类
public class ApplicationContextUtils {
    private static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationContextUtils.applicationContext = applicationContext;
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String beanName) {
        return applicationContext.getBean(beanName);
    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }

    public static <T> T getBean(String beanName, Class<T> clazz) {
        return applicationContext.getBean(beanName, clazz);
    }
}
