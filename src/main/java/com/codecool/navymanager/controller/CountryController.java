package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/country")
public class CountryController {
    @Autowired
    MessageSource messageSource;
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
    public String getDetails(@PathVariable Long id, Model model, Locale locale) {
        CountryDto country = countryService.findById(id, locale);
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
    public ResponseEntity<JsonResponse> add(
            @RequestBody @Valid CountryDto country,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Country.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        countryService.add(country);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {Country.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse> deleteById(@PathVariable Long id, Locale locale) {
        countryService.deleteById(id, locale);
        return ResponseEntity.ok().body(JsonResponse.builder()
                .message(messageSource.getMessage(
                        "removed",
                        new Object[] {Country.class.getSimpleName()},
                        locale)).build());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, Locale locale) {
            CountryDto country = countryService.findById(id, locale);
            model.addAttribute("add", false);
            model.addAttribute("country", country);
            return "country-form";
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> update(
            @PathVariable long id,
            @RequestBody @Valid CountryDto country,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Country.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        countryService.update(country, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {Country.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }
}

