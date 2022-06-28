package com.codecool.navymanager.security;

import com.codecool.navymanager.response.JsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;


@Getter
@Setter
public class JsonLoginFilter extends AbstractAuthenticationProcessingFilter {
    private PasswordEncoder passwordEncoder;
    private MessageSource messageSource;
    protected JsonLoginFilter() {
        super(new AntPathRequestMatcher("/login", "POST"));
        setAuthenticationSuccessHandler((request, response, authentication) -> {
            response.setStatus(HttpServletResponse.SC_OK);
            JsonResponse jsonResponse = JsonResponse.builder().message(
                    messageSource.getMessage(
                            "logged_in",
                            null,
                            request.getLocale())
            ).build();
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(jsonResponse)
            );
        });
        setAuthenticationFailureHandler((request, response, exception) -> {
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
        });
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        String username, password;

        try {
            Map<String, String> requestMap = new ObjectMapper().readValue(request.getInputStream(), Map.class);
            username = requestMap.get("username");
            password = requestMap.get("password");
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage(), e);
        }
        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(
                username, password);

        return this.getAuthenticationManager().authenticate(authRequest);
    }

}
