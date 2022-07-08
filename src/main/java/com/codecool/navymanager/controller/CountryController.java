package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.utilities.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/country")
public class CountryController {
    @Value( "${country.api.mapping}" )
    private String apiMapping;

    private final MessageSource messageSource;
    private final RestTemplate restTemplate;

    public CountryController(MessageSource messageSource, RestTemplate restTemplate) {
        this.messageSource = messageSource;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<CountryDto> countries = restTemplate.exchange(baseUrl + apiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
        model.addAttribute("countries", countries);
        return "country-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, Locale locale, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        CountryDto country = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, CountryDto.class).getBody();
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
    public ResponseEntity<?> addCountryWithForm(
            HttpServletRequest request,
            @RequestBody CountryDto country,
            Model model) {
        HttpEntity<CountryDto> countryHttpEntity =
                Utils.createHttpEntityWithJSessionId(country, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.POST, countryHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            model.addAttribute("add", true);
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, Locale locale, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        CountryDto country = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, CountryDto.class).getBody();
        model.addAttribute("add", false);
        model.addAttribute("country", country);
        return "country-form";
    }

    @RequestMapping(value = "/{id}/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateCountryWithForm(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody CountryDto country,
            Model model) {
        HttpEntity<CountryDto> countryHttpEntity =
                Utils.createHttpEntityWithJSessionId(country, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id, HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            model.addAttribute("add", false);
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

