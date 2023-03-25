package com.yhm.universityhelper.util;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
public class JsonUtils {

    public static void writeJson(HttpServletResponse response, Object object) throws IOException {
        response.setContentType("application/json;charset=utf-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(object));
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String mapToJson(Map<String, Object> map) {
        return new ObjectMapper().writeValueAsString(map);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static Map<String, Object> jsonToMap(String json) {
        return new ObjectMapper().readValue(json, new TypeReference<Map<String, Object>>() {
        });
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> Map<String, T> jsonToMap(String json, Class<T> clazz) {
        return new ObjectMapper().readValue(json, new TypeReference<Map<String, T>>() {
        });
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String objectToJson(Object object) {
        return new ObjectMapper().writeValueAsString(object);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> T jsonToObject(String json, Class<T> clazz) {
        return new ObjectMapper().readValue(json, clazz);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> List<T> jsonToList(String json, Class<T> clazz) {
        return new ObjectMapper().readValue(json, new TypeReference<List<T>>() {
        });
    }

    @SneakyThrows(JsonProcessingException.class)
    public static List<Object> jsonToList(String json) {
        return new ObjectMapper().readValue(json, new TypeReference<List<Object>>() {
        });
    }

    @SneakyThrows(JsonProcessingException.class)
    public static String listToJson(List<Object> list) {
        return new ObjectMapper().writeValueAsString(list);
    }

    @SneakyThrows(JsonProcessingException.class)
    public static <T> String listToJson(List<T> list, Class<T> clazz) {
        return new ObjectMapper().writeValueAsString(list);
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
        return objectToJson(jsonArray);
    }
}
