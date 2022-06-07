package com.codecool.navymanager.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    //todo: should design sidebar, with the tables as categories, top left intersections should be the logo
    @GetMapping
    public String home(){
        return "home";
    }
}
