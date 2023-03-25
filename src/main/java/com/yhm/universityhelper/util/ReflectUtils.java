package com.yhm.universityhelper.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtils {
    public static boolean set(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Object get(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean setBatch(Object obj, Map<String, Object> data) {
        try {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Field field = obj.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(obj, entry.getValue());
            }
            return true;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Map<String, Object> getBatch(Object obj, String... fieldNames) {
        try {
            Map<String, Object> data = new HashMap<>();
            for (String fieldName : fieldNames) {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                data.put(fieldName, field.get(obj));
            }
            return data;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean setBatchWithout(Object obj, Map<String, Object> data, String... fieldNames) {
        try {
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
        } catch (NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static Map<String, Object> getBatchWithout(Object obj, String... fieldNames) {
        try {
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
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
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
}
