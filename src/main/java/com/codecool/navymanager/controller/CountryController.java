package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.utilities.Utils;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping("/country")
public class CountryController {
    private static final String COUNTRY_API_MAPPING = "/country/api";

    @Autowired
    MessageSource messageSource;
    private final CountryService countryService;
    private final RestTemplate restTemplate;

    public CountryController(CountryService countryService, RestTemplate restTemplate) {
        this.countryService = countryService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model) {
        model.addAttribute("countries", countryService.findAll());
        return "country-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, Locale locale) {
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

    @RequestMapping(value="/add-with-form", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addCountry(
            HttpServletRequest request,
            @RequestBody CountryDto country,
            Model model) {
        HttpEntity<CountryDto> countryHttpEntity =
                Utils.createHttpEntityWithSessionJSessionId(country, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + COUNTRY_API_MAPPING, HttpMethod.POST, countryHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            model.addAttribute("add", true);
            model.addAttribute("country", new CountryDto());
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, Locale locale) {
            CountryDto country = countryService.findById(id, locale);
            model.addAttribute("add", false);
            model.addAttribute("country", country);
            return "country-form";
    }

    @RequestMapping(value = "/{id}/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Updates an existing country by id")
    public ResponseEntity<JsonResponse> updateCountry(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody CountryDto country,
            Model model,
            Locale locale) {
        HttpEntity<CountryDto> countryHttpEntity =
                Utils.createHttpEntityWithSessionJSessionId(country, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + COUNTRY_API_MAPPING + "/" + id, HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            model.addAttribute("add", false);
            model.addAttribute("country", new CountryDto());
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

