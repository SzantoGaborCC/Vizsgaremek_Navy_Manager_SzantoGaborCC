package com.codecool.navymanager.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Profile("TEST_AUTH")
public class TestAuthSecurityConfig /*extends WebSecurityConfigurerAdapter*/ {
    @Autowired
    AuthenticationEntryPointImp authenticationEntryPoint;

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
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .and().httpBasic()
                        .and().build();
    }
}
