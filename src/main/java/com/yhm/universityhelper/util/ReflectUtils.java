package com.yhm.universityhelper.util;

import lombok.SneakyThrows;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtils {
    @SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
    public static boolean set(Object obj, String fieldName, Object value) {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(obj, value);
        return true;
    }

    @SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
    public static Object get(Object obj, String fieldName) {
        Field field = obj.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(obj);
    }

    @SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
    public static boolean setBatch(Object obj, Map<String, Object> data) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            Field field = obj.getClass().getDeclaredField(entry.getKey());
            field.setAccessible(true);
            field.set(obj, entry.getValue());
        }
        return true;
    }

    @SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
    public static Map<String, Object> getBatch(Object obj, String... fieldNames) {
        Map<String, Object> data = new HashMap<>();
        for (String fieldName : fieldNames) {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            data.put(fieldName, field.get(obj));
        }
        return data;
    }

    @SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
    public static boolean setBatchWithout(Object obj, Map<String, Object> data, String... fieldNames) {
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            boolean flag = true;
            for (String fieldName : fieldNames) {
                if (entry.getKey().equals(fieldName)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                Field field = obj.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(obj, entry.getValue());
            }
        }
        return true;
    }

    @SneakyThrows({NoSuchFieldException.class, IllegalAccessException.class})
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
                field.setAccessible(true);
                data.put(field.getName(), field.get(obj));
            }
        }
        return data;
    }
}
