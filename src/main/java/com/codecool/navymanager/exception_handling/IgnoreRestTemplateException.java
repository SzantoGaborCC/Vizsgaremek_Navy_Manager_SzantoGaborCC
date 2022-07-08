package com.codecool.navymanager.exception_handling;

import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;

public class IgnoreRestTemplateException extends DefaultResponseErrorHandler {
    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
    }
}
