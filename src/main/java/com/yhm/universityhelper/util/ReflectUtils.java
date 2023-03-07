package com.yhm.universityhelper.util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtils {
    public static boolean set(@NotNull Object obj, @NotNull String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(obj, value);
            return true;
        } catch (@NotNull NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static @Nullable Object get(@NotNull Object obj, @NotNull String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (@NotNull NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean setBatch(@NotNull Object obj, @NotNull Map<String, Object> data) {
        try {
            for (Map.Entry<String, Object> entry : data.entrySet()) {
                Field field = obj.getClass().getDeclaredField(entry.getKey());
                field.setAccessible(true);
                field.set(obj, entry.getValue());
            }
            return true;
        } catch (@NotNull NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static @Nullable Map<String, Object> getBatch(@NotNull Object obj, String @NotNull ... fieldNames) {
        try {
            Map<String, Object> data = new HashMap<>();
            for (String fieldName : fieldNames) {
                Field field = obj.getClass().getDeclaredField(fieldName);
                field.setAccessible(true);
                data.put(fieldName, field.get(obj));
            }
            return data;
        } catch (@NotNull NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean setBatchWithout(@NotNull Object obj, @NotNull Map<String, Object> data, String @NotNull ... fieldNames) {
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
        } catch (@NotNull NoSuchFieldException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static @Nullable Map<String, Object> getBatchWithout(@NotNull Object obj, String @NotNull ... fieldNames) {
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
}
