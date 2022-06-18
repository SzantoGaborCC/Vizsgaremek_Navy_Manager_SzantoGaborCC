package com.codecool.navymanager.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@Builder
public class JsonResponse {
    private String message;
    private String errorDescription;
    private Map<String, String> errorMessages;
}