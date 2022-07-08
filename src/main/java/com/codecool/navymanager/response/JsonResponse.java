package com.codecool.navymanager.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
//@Builder
public class JsonResponse {
    private String message;
    private String errorDescription;
    private Map<String, String> errorMessages;

    public JsonResponse() {
    }

    @Override
    public String toString() {
        return "JsonResponse{" +
                "message='" + message + '\'' +
                ", errorDescription='" + errorDescription + '\'' +
                ", errorMessages=" + errorMessages +
                '}';
    }
}
