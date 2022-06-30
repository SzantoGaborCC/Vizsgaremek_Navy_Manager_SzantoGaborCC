package com.codecool.navymanager.security;

import com.codecool.navymanager.exception_handling.SecurityExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Profile("TEST_AUTH")
public class TestAuthSecurityConfig /*extends WebSecurityConfigurerAdapter*/ {
    @Autowired
    SecurityExceptionHandler securityExceptionHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return  // Disable CSRF
                http.csrf().disable()
                        .authorizeRequests()
                        .antMatchers(HttpMethod.GET).permitAll()
                        .antMatchers(HttpMethod.POST, "/login").permitAll()
                        .anyRequest().authenticated()
                        .and()
                        .exceptionHandling()
                        .authenticationEntryPoint(securityExceptionHandler)
                        .and().httpBasic()
                        .and().build();
    }
}
