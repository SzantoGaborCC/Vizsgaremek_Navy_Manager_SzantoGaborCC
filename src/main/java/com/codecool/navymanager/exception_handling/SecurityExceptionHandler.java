package com.codecool.navymanager.exception_handling;

import com.codecool.navymanager.response.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class SecurityExceptionHandler implements AuthenticationEntryPoint {
    @Autowired
    MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonResponse jsonResponse = JsonResponse.builder().errorDescription(
                messageSource.getMessage(
                        "unauthorized",
                        null,
                        request.getLocale())
        ).build();
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(jsonResponse)
        );
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public void commence2(HttpServletRequest request, HttpServletResponse response,
                         AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonResponse jsonResponse = JsonResponse.builder().errorDescription(
                messageSource.getMessage(
                        "forbidden",
                        null,
                        request.getLocale())
        ).build();
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(jsonResponse)
        );
    }
}