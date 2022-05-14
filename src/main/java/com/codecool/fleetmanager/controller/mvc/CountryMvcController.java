package com.codecool.fleetmanager.controller.mvc;


import com.codecool.fleetmanager.model.Country;
import com.codecool.fleetmanager.service.CountryService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/country-mvc")
public class CountryMvcController {
    private CountryService countryService;

    public CountryMvcController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public String listCountrys(Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "country-list";
    }

    @GetMapping("/{id}")
    public Country findById(@PathVariable Long id) {
        return countryService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(Country country, Model model){
        model.addAttribute("create", true);
        return "country-form";
    }

    @PostMapping("/create")
    public String add(@Valid Country country, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            return "country-form";
        }
        try {
           countryService.add(country);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid country data!", e);
        }
        return "redirect:/country-mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        countryService.delete(id);
        return "redirect:/country-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            Country country = countryService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("country", country);
            return "country-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship class!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @Valid Country country, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "country-form";
        }
        countryService.update(country, id);
        return "redirect:/country-mvc";
    }

    @InitBinder
    public void registerDateFormatter(WebDataBinder binder) {
        binder.registerCustomEditor(
                Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }
}

