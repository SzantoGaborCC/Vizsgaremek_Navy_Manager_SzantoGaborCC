package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.RankDto;
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
@RequestMapping("/officer")
public class OfficerController {

    private final RestTemplate restTemplate;

    @Value("${country.api.mapping}")
    private String countryApiMapping;

    @Value( "${rank.api.mapping}" )
    private String rankApiMapping;

    @Value("${officer.api.mapping}")
    private String apiMapping;

    public OfficerController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<OfficerDto> officers = restTemplate.exchange(baseUrl + apiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<OfficerDto>>() {}).getBody();
        model.addAttribute("officers", officers);
        return "officer-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        OfficerDto officer = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, OfficerDto.class).getBody();
        model.addAttribute("officer", officer);
        return "officer-details";
    }


    @GetMapping("/show-add-form")
    public String showAddForm(Model model, HttpServletRequest request){
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<CountryDto> validCountryValues = restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
        List<RankDto> validRankValues = restTemplate.exchange(baseUrl + rankApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RankDto>>() {}).getBody();
        model.addAttribute("officer", new OfficerDto());
        model.addAttribute("add", true);
        model.addAttribute("validRankValues", validRankValues);
        model.addAttribute("validCountryValues", validCountryValues);
        return "officer-form";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> addOfficerWithForm(
            @RequestBody OfficerDto officer,
            HttpServletRequest request) {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntityWithJSessionId(officer, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.POST, officerHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }


    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id,Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        OfficerDto officer = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, OfficerDto.class).getBody();
        List<CountryDto> validCountryValues =
                restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
        List<RankDto> validRankValues =
                restTemplate.exchange(baseUrl + rankApiMapping, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<RankDto>>() {}).getBody();
        model.addAttribute("add", false);
        model.addAttribute("officer", officer);
        model.addAttribute("validRankValues", validRankValues);
        model.addAttribute("validCountryValues", validCountryValues);
        return "officer-form";
    }

    @RequestMapping(value = "/{id}/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateGunWithForm(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody OfficerDto officer) {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntityWithJSessionId(officer, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id, HttpMethod.PUT, officerHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

