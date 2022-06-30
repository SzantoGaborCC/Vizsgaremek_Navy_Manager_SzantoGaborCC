package com.codecool.navymanager;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class TestUtilities {
    public static <T> HttpEntity<T> createHttpEntity(T data) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(data, httpHeaders);
    }
}
