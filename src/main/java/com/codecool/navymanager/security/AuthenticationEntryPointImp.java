package com.codecool.navymanager.security;

import com.codecool.navymanager.response.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImp implements AuthenticationEntryPoint {
    @Autowired
    MessageSource messageSource;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setErrorDescription(
                messageSource.getMessage(
                        "unauthorized",
                        null,
                        request.getLocale())
        );
        response.getWriter().write(
                new ObjectMapper().writeValueAsString(jsonResponse)
        );
    }
}
