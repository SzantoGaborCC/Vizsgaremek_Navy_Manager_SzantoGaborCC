package com.codecool.navymanager.configuration;

import com.codecool.navymanager.exception_handling.IgnoreRestTemplateException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfigurer {
    @Bean
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new IgnoreRestTemplateException());
        return restTemplate;
    }
}
