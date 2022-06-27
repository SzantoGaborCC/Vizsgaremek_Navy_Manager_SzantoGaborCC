package com.codecool.navymanager.controller;

import com.codecool.navymanager.dto.LoginData;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.response.JsonResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
public class LoginController {
    @Autowired
    MessageSource messageSource;

    @GetMapping("/login")
    public String showLoginFormOrRedirectIfLoggedIn(@AuthenticationPrincipal UserDetails userDetails, Model model, Locale locale) {
        if (userDetails == null) {
            model.addAttribute("login", new LoginData());
            return "login-form";
        } else {
            return "redirect:/";
        }
    }
}
