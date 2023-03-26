package com.yhm.universityhelper.util;

import cn.hutool.core.util.ReflectUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtils {
    public static void set(Object obj, String fieldName, Object value) {
        ReflectUtil.setFieldValue(obj, fieldName, value);
    }

    public static Object get(Object obj, String fieldName) {
        return ReflectUtil.getFieldValue(obj, fieldName);
    }

    public static void setBatch(Object obj, Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            ReflectUtil.setFieldValue(obj, entry.getKey(), entry.getValue());
        }
    }

    public static Map<String, Object> getBatch(Object obj, String... fieldNames) {
        Map<String, Object> data = new HashMap<>();
        for (String fieldName : fieldNames) {
            data.put(fieldName, ReflectUtil.getFieldValue(obj, fieldName));
        }
        return data;
    }

    public static void setBatchWithout(Object obj, Map<String, Object> data, String... fieldNames) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            boolean flag = true;
            for (String fieldName : fieldNames) {
                if (entry.getKey().equals(fieldName)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                ReflectUtil.setFieldValue(obj, entry.getKey(), entry.getValue());
            }
        }
    }

    public static Map<String, Object> getBatchWithout(Object obj, String... fieldNames) {
        Map<String, Object> data = new HashMap<>();
        for (Field field : obj.getClass().getDeclaredFields()) {
            boolean flag = true;
            for (String fieldName : fieldNames) {
                if (field.getName().equals(fieldName)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                data.put(field.getName(), ReflectUtil.getFieldValue(obj, field.getName()));
            }
        }
        return data;
    }

    // call
    public static <T> T call(Object obj, String methodName, Class<T> returnType, Object... args) {
        try {
            Class<?>[] argTypes = new Class<?>[args.length];
            for (int i = 0; i < args.length; i++) {
                argTypes[i] = args[i].getClass();
            }
            return returnType.cast(obj.getClass().getDeclaredMethod(methodName, argTypes).invoke(obj, args));
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static <T> T call(Object obj, String methodName, Object... args) {
        return ReflectUtil.invoke(obj, methodName, args);
    }

    public static <T> T callStatic(Class<?> clazz, String methodName, Object... args) {
        Method method = ReflectUtil.getMethodByNameIgnoreCase(clazz, methodName);
        return ReflectUtil.invokeStatic(method, args);
    }
}
