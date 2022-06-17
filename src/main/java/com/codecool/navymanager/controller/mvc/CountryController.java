package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.response.Response;
import com.codecool.navymanager.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/country")
public class CountryController {
    private final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public String listCountries(Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "country-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        CountryDto country = countryService.findById(id);
        model.addAttribute("country", country);
        return "country-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("country", new CountryDto());
        return "country-form";
    }

    @PostMapping
    public ResponseEntity<Response> add(@ModelAttribute("country") @Valid CountryDto country, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid country data!");
            return ResponseEntity.badRequest().body(response);
        }
        countryService.add(country);
        response.setMessage("Country was added.");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        countryService.deleteById(id);
        return ResponseEntity.ok().body("Country was removed.");
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model) {
            CountryDto country = countryService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("country", country);
            return "country-form";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable long id, @ModelAttribute("country") @Valid CountryDto country, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid country data!");
            return ResponseEntity.badRequest().body(response);
        }
        countryService.update(country, id);
        response.setMessage("Country was updated.");
        return ResponseEntity.ok().body(response);
    }
}

