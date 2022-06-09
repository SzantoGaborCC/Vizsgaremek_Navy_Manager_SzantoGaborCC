package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.entityDTO.CountryDto;
import com.codecool.navymanager.service.CountryService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/country-mvc")
public class CountryMvcController {
    private final CountryService countryService;

    public CountryMvcController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public String listCountries(Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "country-list";
    }

    @GetMapping("/{id}")
    public CountryDto findById(@PathVariable Long id) {
        return countryService.findById(id);
    }

    @GetMapping("/add")
    public String showCreateForm(Model model){
        model.addAttribute("country", new CountryDto());
        model.addAttribute("add", true);
        return "country-form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("country") @Valid CountryDto country, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("add", true);
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
        countryService.deleteById(id);
        return "redirect:/country-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            CountryDto country = countryService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("country", country);
            return "country-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship class!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("country") @Valid CountryDto country, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            return "country-form";
        }
        countryService.update(country, id);
        return "redirect:/country-mvc";
    }

  /*  @InitBinder
    public void registerDateFormatter(WebDataBinder binder) {
        binder.registerCustomEditor(
                Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }*/
}

