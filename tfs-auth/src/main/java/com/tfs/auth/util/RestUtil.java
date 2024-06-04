package com.tfs.auth.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class RestUtil {

    private RestTemplate restTemplate;

    @Autowired
    public RestUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> T get(String url, Class<T> clazz) {
        return restTemplate.getForObject(url, clazz);
    }

    public <T> T post(String url, Object request, Class<T> clazz) {
        return restTemplate.postForObject(url, request, clazz);
    }

    public <T> T post(String url, HttpEntity<?> httpEntity, Class<T> clazz) {
        return restTemplate.postForEntity(url, httpEntity, clazz).getBody();
    }

    public <T> T request(RequestEntity<?> requestEntity, Class<T> clazz) {
        return restTemplate.exchange(requestEntity, clazz).getBody();
    }
}
