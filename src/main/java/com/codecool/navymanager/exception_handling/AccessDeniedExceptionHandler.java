package com.codecool.navymanager.exception_handling;

import com.codecool.navymanager.response.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class AccessDeniedExceptionHandler {
    @Autowired
    MessageSource messageSource;

    @ExceptionHandler(value = {AccessDeniedException.class})
    public void commence2(HttpServletRequest request, HttpServletResponse response,
                         AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        JsonResponse jsonResponse = new JsonResponse();
                jsonResponse.setErrorDescription(
                messageSource.getMessage(
                        "forbidden",
                        null,
                        request.getLocale())
        );
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(jsonResponse)
        );
    }
}