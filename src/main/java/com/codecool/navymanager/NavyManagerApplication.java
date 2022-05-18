package com.codecool.navymanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class NavyManagerApplication {
    public static void main(String[] args) {
        SpringApplication.run(NavyManagerApplication.class, args);
    }
}
