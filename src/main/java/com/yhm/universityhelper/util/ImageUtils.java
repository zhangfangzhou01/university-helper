package com.yhm.universityhelper.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.converter.BufferedImageHttpMessageConverter;
import org.springframework.http.converter.ByteArrayHttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.springframework.http.MediaType.*;

@Component
public class ImageUtils {
    @Value("${imageServerUrl}")
    private String imageServerUrl;

    private static final HttpHeaders JSON_HEADER;

    private static final HttpHeaders IMAGE_HEADER;
    
    private static final HttpHeaders FILE_HEADER;
    
    private static final RestTemplate restTemplate;

    static {
        JSON_HEADER = new HttpHeaders();
        IMAGE_HEADER = new HttpHeaders();
        FILE_HEADER = new HttpHeaders();
        restTemplate = new RestTemplate();
        JSON_HEADER.setContentType(APPLICATION_JSON);
        IMAGE_HEADER.setContentType(IMAGE_JPEG);
        FILE_HEADER.setContentType(MULTIPART_FORM_DATA);
        restTemplate.getMessageConverters().add(new ByteArrayHttpMessageConverter());
        restTemplate.getMessageConverters().add(new BufferedImageHttpMessageConverter());
        restTemplate.getMessageConverters().add(new ResourceHttpMessageConverter());
    }

    public void uploadImage(Long id, String type, Resource file) {
        RestTemplate restTemplate = new RestTemplate();
        MultiValueMap<String,Object> params = new LinkedMultiValueMap<>();
        params.add("images", file);
        HttpEntity<MultiValueMap<String,Object>> request = new HttpEntity<>(params, FILE_HEADER);

        restTemplate.postForObject(imageServerUrl + "/upload/" + type + "/" + id, request, String.class);
    }

    public List downloadImage(Long id, String type) {
        RestTemplate restTemplate = new RestTemplate();
        return restTemplate.getForObject(imageServerUrl + "/download?id=" + id + "type=" + type, List.class);
    }

    public void deleteImage(Long id, String type) {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.delete(imageServerUrl + "/delete?id=" + id + "type=" + type);
    }
}