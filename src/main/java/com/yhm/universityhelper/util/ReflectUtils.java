package com.yhm.universityhelper.util;

import com.esotericsoftware.reflectasm.MethodAccess;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtils {
    private static final Map<Class<?>, Map<MethodAccess, Map<String, Integer>>> METHOD_ACCESS_CACHE = new HashMap<>();

    public static void set(Object obj, String fieldName, Object value) {
        Class<?> clazz = obj.getClass();
        Map<MethodAccess, Map<String, Integer>> methodAccessMap = METHOD_ACCESS_CACHE.computeIfAbsent(clazz, k -> Collections.singletonMap(MethodAccess.get(clazz), new HashMap<>()));
        MethodAccess methodAccess = methodAccessMap.keySet().iterator().next();

        int index = methodAccessMap.get(methodAccess).computeIfAbsent(fieldName, k -> methodAccess.getIndex("set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), value.getClass()));
        methodAccess.invoke(obj, index, value);
    }

    public static Object get(Object obj, String fieldName) {
        Class<?> clazz = obj.getClass();
        Map<MethodAccess, Map<String, Integer>> methodAccessMap = METHOD_ACCESS_CACHE.computeIfAbsent(clazz, k -> Collections.singletonMap(MethodAccess.get(clazz), new HashMap<>()));
        MethodAccess methodAccess = methodAccessMap.keySet().iterator().next();

        int index = methodAccessMap.get(methodAccess).computeIfAbsent(fieldName, k -> methodAccess.getIndex("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)));
        return methodAccess.invoke(obj, index);
    }

    public static void setBatch(Object obj, Map<String, Object> data) {
        Class<?> clazz = obj.getClass();
        Map<MethodAccess, Map<String, Integer>> methodAccessMap = METHOD_ACCESS_CACHE.computeIfAbsent(clazz, k -> Collections.singletonMap(MethodAccess.get(clazz), new HashMap<>()));
        MethodAccess methodAccess = methodAccessMap.keySet().iterator().next();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            int index = methodAccessMap.get(methodAccess).computeIfAbsent(entry.getKey(), k -> methodAccess.getIndex("set" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), entry.getValue().getClass()));
            methodAccess.invoke(obj, index, entry.getValue());
        }
    }

    public static Map<String, Object> getBatch(Object obj, String... fieldNames) {
        Class<?> clazz = obj.getClass();
        Map<MethodAccess, Map<String, Integer>> methodAccessMap = METHOD_ACCESS_CACHE.computeIfAbsent(clazz, k -> Collections.singletonMap(MethodAccess.get(clazz), new HashMap<>()));
        MethodAccess methodAccess = methodAccessMap.keySet().iterator().next();

        Map<String, Object> data = new HashMap<>();
        for (String fieldName : fieldNames) {
            int index = methodAccessMap.get(methodAccess).computeIfAbsent(fieldName, k -> methodAccess.getIndex("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1)));
            data.put(fieldName, methodAccess.invoke(obj, index));
        }
        return data;
    }

    public static void setBatchWithout(Object obj, Map<String, Object> data, String... fieldNames) {
        Class<?> clazz = obj.getClass();
        Map<MethodAccess, Map<String, Integer>> methodAccessMap = METHOD_ACCESS_CACHE.computeIfAbsent(clazz, k -> Collections.singletonMap(MethodAccess.get(clazz), new HashMap<>()));
        MethodAccess methodAccess = methodAccessMap.keySet().iterator().next();

        for (Map.Entry<String, Object> entry : data.entrySet()) {
            boolean flag = true;
            for (String fieldName : fieldNames) {
                if (entry.getKey().equals(fieldName)) {
                    flag = false;
                    break;
                }
            }
            if (flag) {
                int index = methodAccessMap.get(methodAccess).computeIfAbsent(entry.getKey(), k -> methodAccess.getIndex("set" + entry.getKey().substring(0, 1).toUpperCase() + entry.getKey().substring(1), entry.getValue().getClass()));
                methodAccess.invoke(obj, index, entry.getValue());
            }
        }
    }

    public static Map<String, Object> getBatchWithout(Object obj, String... fieldNames) {
        Class<?> clazz = obj.getClass();
        Map<MethodAccess, Map<String, Integer>> methodAccessMap = METHOD_ACCESS_CACHE.computeIfAbsent(clazz, k -> Collections.singletonMap(MethodAccess.get(clazz), new HashMap<>()));
        MethodAccess methodAccess = methodAccessMap.keySet().iterator().next();

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
                int index = methodAccessMap.get(methodAccess).computeIfAbsent(field.getName(), k -> methodAccess.getIndex("get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1)));
                data.put(field.getName(), methodAccess.invoke(obj, index));
            }
        }
        return data;
    }

    public static <T> T call(Object obj, String methodName, Class<T> returnType, Object... args) {
        Class<?> clazz = obj.getClass();
        Map<MethodAccess, Map<String, Integer>> methodAccessMap = METHOD_ACCESS_CACHE.computeIfAbsent(clazz, k -> Collections.singletonMap(MethodAccess.get(clazz), new HashMap<>()));
        MethodAccess methodAccess = methodAccessMap.keySet().iterator().next();

        Class<?>[] argTypes = new Class<?>[args.length];
        for (int i = 0; i < args.length; i++) {
            argTypes[i] = args[i].getClass();
        }
        int index = methodAccessMap.get(methodAccess).computeIfAbsent(methodName, k -> methodAccess.getIndex(methodName, argTypes));
        return returnType.cast(methodAccess.invoke(obj, index, args));
    }
}
