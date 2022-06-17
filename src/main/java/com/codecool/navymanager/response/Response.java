package com.codecool.navymanager.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Response {
    private String message;
    private String errorDescription;
    private Map<String, String> errorMessages;
}
