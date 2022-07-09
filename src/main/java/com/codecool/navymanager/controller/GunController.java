package com.codecool.navymanager.controller;

import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.utilities.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/gun")
public class GunController {

    private final RestTemplate restTemplate;

    @Value("${country.api.mapping}")
    private String countryApiMapping;

    @Value("${gun.api.mapping}")
    private String apiMapping;

    public GunController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<GunDto> guns = restTemplate.exchange(baseUrl + apiMapping,
                HttpMethod.GET, null, new ParameterizedTypeReference<List<GunDto>>() {
                }).getBody();
        model.addAttribute("guns", guns);
        return "gun-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        GunDto gun = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, GunDto.class).getBody();
        model.addAttribute("gun", gun);
        return "gun-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<CountryDto> validCountryValues =
                restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<CountryDto>>() {
                        }).getBody();
        model.addAttribute("add", true);
        model.addAttribute("gun", new GunDto());
        model.addAttribute("validCountryValues", validCountryValues);
        return "gun-form";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> addGunWithForm(
            @RequestBody GunDto gun,
            HttpServletRequest request) {
        HttpEntity<GunDto> gunHttpEntity =
            Utils.createHttpEntityWithJSessionId(gun, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.POST, gunHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        GunDto gun = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, GunDto.class).getBody();
        List<CountryDto> validCountryValues = restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryDto>>() {
                }).getBody();
        model.addAttribute("add", false);
        model.addAttribute("gun", gun);
        model.addAttribute("validCountryValues", validCountryValues);
        return "gun-form";
    }

    @RequestMapping(value = "/{id}/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateGunWithForm(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody GunDto gun) {
        HttpEntity<GunDto> gunHttpEntity =
                Utils.createHttpEntityWithJSessionId(gun, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id, HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

