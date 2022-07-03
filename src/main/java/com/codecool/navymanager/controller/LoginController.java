package com.codecool.navymanager.controller;

import com.codecool.navymanager.dto.LoginData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    MessageSource messageSource;

    @GetMapping("/login")
    public String showLoginFormOrRedirectIfLoggedIn(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        if (userDetails == null) {
            model.addAttribute("login", new LoginData());
            return "login-form";
        } else {
            return "redirect:/";
        }
    }
}
