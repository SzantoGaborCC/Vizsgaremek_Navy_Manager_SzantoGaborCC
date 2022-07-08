package com.codecool.navymanager.utilities;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    public static <T> HttpEntity<T> createHttpEntity(T data) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(data, httpHeaders);
    }

    public static <T> HttpEntity<T> createHttpEntityWithJSessionId(T data, String sessionId) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        httpHeaders.add("Cookie", "JSESSIONID=" + sessionId);
        return new HttpEntity<>(data, httpHeaders);
    }

    public static String getBaseUrlFromRequest(HttpServletRequest request) {
        return ServletUriComponentsBuilder.fromRequestUri(request)
                .replacePath(null)
                .build()
                .toUriString();
    }
}
