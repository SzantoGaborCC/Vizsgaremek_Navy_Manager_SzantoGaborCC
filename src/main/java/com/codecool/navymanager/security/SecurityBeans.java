package com.codecool.navymanager.security;

import com.codecool.navymanager.exception_handling.SecurityExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@EnableWebSecurity
public class SecurityBeans {

    @Autowired
    MessageSource messageSource;

    @Autowired
    AuthenticationConfiguration authenticationConfiguration;

    @Autowired
    SecurityExceptionHandler securityExceptionHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        JsonLoginFilter jsonLoginFilter = new JsonLoginFilter();
        jsonLoginFilter.setAuthenticationManager(authenticationConfiguration.getAuthenticationManager());
        jsonLoginFilter.setPasswordEncoder(passwordEncoder());
        jsonLoginFilter.setMessageSource(messageSource);

        http
                .addFilterAt(jsonLoginFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations
                        ()).permitAll()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .antMatchers(HttpMethod.POST, "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(securityExceptionHandler)
                .and()
               // .formLogin().permitAll()
               // .loginPage("/login")
              //  .and()
             //   .formLogin().permitAll()
              //  .and()
             //   .logout().permitAll()
             //   .httpBasic()
                .csrf().disable()
                .cors();
        return http.build();
    }
}
