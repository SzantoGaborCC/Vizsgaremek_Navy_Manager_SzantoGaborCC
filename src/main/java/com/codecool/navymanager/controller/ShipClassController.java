package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.*;
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
import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/ship-class")
public class ShipClassController {

    @Value( "${ship-class.api.mapping}" )
    private String apiMapping;

    @Value( "${hull-classification.api.mapping}" )
    private String hullClassificationApiMapping;

    @Value( "${gun.api.mapping}" )
    private String gunApiMapping;

    @Value( "${country.api.mapping}" )
    private String countryApiMapping;

    private final RestTemplate restTemplate;

    public ShipClassController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<ShipClassDto> shipClasses = restTemplate.exchange(baseUrl + apiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<ShipClassDto>>() {}).getBody();
        model.addAttribute("shipClasses", shipClasses);
        return "ship-class-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ShipClassDto shipClass = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, ShipClassDto.class).getBody();
        List<GunDto> validGunValues = restTemplate.exchange(baseUrl + apiMapping + "/" + id + "/valid-guns",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<GunDto>>() {}).getBody();
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("validGunValues", validGunValues);
        return "ship-class-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<CountryDto> validCountryValues = restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
        List<HullClassificationDto> validHullClassificationValues =
                restTemplate.exchange(baseUrl + hullClassificationApiMapping, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<HullClassificationDto>>() {}).getBody();
        model.addAttribute("add", true);
        model.addAttribute("shipClass", new ShipClassDto());
        model.addAttribute("validCountryValues", validCountryValues);
        model.addAttribute("validHullClassificationValues", validHullClassificationValues);
        return "ship-class-form";
    }

    @RequestMapping(value = "/add-with-form" ,method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> addShipClassWithForm(
            HttpServletRequest request,
            @RequestBody ShipClassDto shipClass,
            Model model) {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntityWithJSessionId(shipClass, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.POST, shipClassHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            List<CountryDto> validCountryValues = restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
            List<HullClassificationDto> validHullClassificationValues =
                    restTemplate.exchange(baseUrl + hullClassificationApiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<HullClassificationDto>>() {}).getBody();
            model.addAttribute("add", true);
            model.addAttribute("validCountryValues", validCountryValues);
            model.addAttribute("validHullClassificationValues", validHullClassificationValues);
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, HttpServletRequest request) {
            String baseUrl = Utils.getBaseUrlFromRequest(request);
            ShipClassDto shipClass = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, ShipClassDto.class).getBody();
            List<CountryDto> validCountryValues = restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
            List<HullClassificationDto> validHullClassificationValues =
                    restTemplate.exchange(baseUrl + hullClassificationApiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<HullClassificationDto>>() {}).getBody();
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validCountryValues", validCountryValues);
            model.addAttribute("validHullClassificationValues", validHullClassificationValues);
            return "ship-class-form";
    }

    @RequestMapping(value = "{id}/update-with-form" ,method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateShipClassWithForm(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody ShipClassDto shipClass,
            Model model) {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntityWithJSessionId(shipClass, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id, HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            List<CountryDto> validCountryValues = restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
            List<HullClassificationDto> validHullClassificationValues =
                    restTemplate.exchange(baseUrl + hullClassificationApiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<HullClassificationDto>>() {}).getBody();
            model.addAttribute("add", true);
            model.addAttribute("validCountryValues", validCountryValues);
            model.addAttribute("validHullClassificationValues", validHullClassificationValues);
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/gun/show-add-gun-form")
    public String showAddGunForm(
            @PathVariable Long id,
            Model model,
            HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ShipClassDto shipClass = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, ShipClassDto.class).getBody();
        List<GunDto> validGunValues = restTemplate.exchange(baseUrl + apiMapping + "/" + id + "/valid-guns",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<GunDto>>() {}).getBody();
        model.addAttribute("add", true);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunInstallation", new GunInstallationDto());
        model.addAttribute("validGunValues", validGunValues);
        return "ship-class-gun-form";
    }

    @RequestMapping(value = "/{id}/gun" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> addGun(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody GunInstallationDto gunInstallation,
            Model model) {
        HttpEntity<GunInstallationDto> gunHttpEntity =
                Utils.createHttpEntityWithJSessionId(
                        gunInstallation,
                        RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id + "/gun", HttpMethod.POST, gunHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            ShipClassDto shipClass = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, ShipClassDto.class).getBody();
            List<GunDto> validGunValues = restTemplate.exchange(baseUrl + apiMapping + "/" + id + "/valid-guns",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<GunDto>>() {}).getBody();
            model.addAttribute("add", true);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", validGunValues);
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{shipClassId}/gun/{gunId}/show-update-gun-form")
    public String showUpdateGunForm(
            @PathVariable long shipClassId,
            @PathVariable long gunId,
            Model model,
            HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ShipClassDto shipClass =
                restTemplate.getForEntity(baseUrl + apiMapping + "/" + shipClassId, ShipClassDto.class).getBody();
        GunInstallationDto gunInstallationDto =
                restTemplate.getForEntity(baseUrl + apiMapping + "/" + shipClassId + "/gun/" + gunId,
                        GunInstallationDto.class).getBody();
        List<GunDto> validGunValues = restTemplate.exchange(baseUrl + apiMapping + "/" + shipClassId + "/valid-guns",
                HttpMethod.GET, null, new ParameterizedTypeReference<List<GunDto>>() {}).getBody();
        model.addAttribute("add", false);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunInstallation", gunInstallationDto);
        model.addAttribute("validGunValues", validGunValues);
        return "ship-class-gun-form";
    }

    @RequestMapping(value = "/{id}/gun/{gunId}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateGun(
            HttpServletRequest request,
            @PathVariable long id, @PathVariable long gunId,
            @RequestBody @Valid GunInstallationDto gunInstallation,
            Model model) {
        HttpEntity<GunInstallationDto> gunHttpEntity =
                Utils.createHttpEntityWithJSessionId(
                        gunInstallation,
                        RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(
                        baseUrl + apiMapping + "/" + id + "/gun/" + gunId,
                        HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            ShipClassDto shipClass =
                    restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, ShipClassDto.class).getBody();
            GunDto gun =
                    restTemplate.getForEntity(baseUrl + gunApiMapping + "/" + gunId, GunDto.class).getBody();
            List<GunDto> validGunValues = restTemplate.exchange(baseUrl + apiMapping + "/" + id + "/valid-guns",
                    HttpMethod.GET, null, new ParameterizedTypeReference<List<GunDto>>() {}).getBody();
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("gun", gun);
            model.addAttribute("validGunValues", validGunValues);
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

