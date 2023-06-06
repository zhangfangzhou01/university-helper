package com.yhm.universityhelper.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class JsonUtils {
    private static final AtomicReference<ObjectMapper> OBJECT_MAPPER;
    
    static {
        OBJECT_MAPPER = new AtomicReference<>(new ObjectMapper());
        OBJECT_MAPPER.get().registerModule(new JavaTimeModule());
    }
    
    public static void writeJson(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(OBJECT_MAPPER.get().writeValueAsString(object));
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String mapToJson(Map<String, Object> map) {
        return OBJECT_MAPPER.get().writeValueAsString(map);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static Map<String, Object> jsonToMap(String json) {
        return OBJECT_MAPPER.get().readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> Map<String, T> jsonToMap(String json, Class<T> clazz) {
        return OBJECT_MAPPER.get().readValue(json, new TypeReference<Map<String, T>>() {
        });
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String objectToJson(Object object) {
        return OBJECT_MAPPER.get().writeValueAsString(object);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return OBJECT_MAPPER.get().readValue(json, clazz);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        return OBJECT_MAPPER.get().readValue(json, new TypeReference<List<T>>() {
        });
    }

    @SneakyThrows(JsonProcessingException.class)
    public static List<Object> jsonToList(String json) {
        return OBJECT_MAPPER.get().readValue(json, new TypeReference<List<Object>>() {
        });
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String listToJson(List<Object> list) {
        return OBJECT_MAPPER.get().writeValueAsString(list);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> String listToJson(List<T> list, Class<T> clazz) {
        return OBJECT_MAPPER.get().writeValueAsString(list);
    }

    public static JSONObject jsonToJsonObject(String json) {
        return new JSONObject(json);
    }

    public static String jsonObjectToJson(JSONObject jsonObject) {
        return objectToJson(jsonObject);
    }

    public static JSONArray jsonToJsonArray(String json) {
        return new JSONArray(json);
    }

    public static String jsonArrayToJson(JSONArray jsonArray) {
        return jsonArray.toString();
    }
    
    public static JSONObject entityToJsonObject(Object object) {
        return OBJECT_MAPPER.get().convertValue(object, JSONObject.class);
    } 
}
