package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.dto.GunInstallationDto;
import com.codecool.navymanager.dto.ShipClassDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.GunService;
import com.codecool.navymanager.service.HullClassificationService;
import com.codecool.navymanager.service.ShipClassService;
import com.codecool.navymanager.utilities.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Locale;

@Controller
@RequestMapping("/ship-class")
public class ShipClassController {

    @Value( "${ship-class.api.mapping}" )
    private String apiMapping;
    private final MessageSource messageSource;

    private final ShipClassService shipClassService;

    private final GunService gunService;

    private final HullClassificationService hullClassificationService;
    private final CountryService countryService;
    private final RestTemplate restTemplate;

    public ShipClassController(
            MessageSource messageSource,
            ShipClassService shipClassService,
            GunService gunService,
            HullClassificationService hullClassificationService,
            CountryService countryService,
            RestTemplate restTemplate) {
        this.messageSource = messageSource;
        this.shipClassService = shipClassService;
        this.gunService = gunService;
        this.hullClassificationService = hullClassificationService;
        this.countryService = countryService;
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model) {
        model.addAttribute("shipClasses", shipClassService.findAll());
        return "ship-class-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, Locale locale) {
        ShipClassDto shipClass = shipClassService.findById(id, locale);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
        return "ship-class-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model) {
        model.addAttribute("add", true);
        model.addAttribute("shipClass", new ShipClassDto());
        model.addAttribute("validCountryValues", countryService.findAll());
        model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
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
            model.addAttribute("add", true);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, Locale locale) {
            ShipClassDto shipClass = shipClassService.findById(id, locale);
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
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
            model.addAttribute("add", true);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/gun/show-add-gun-form")
    public String showAddGunForm(
            @PathVariable Long id,
            Model model,
            Locale locale) {
        ShipClassDto shipClass = shipClassService.findById(id, locale);
        model.addAttribute("add", true);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunInstallation", new GunInstallationDto());
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
        return "ship-class-gun-form";
    }

    @RequestMapping(value = "/{id}/gun" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> addGun(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody GunInstallationDto gunInstallation,
            Model model,
            Locale locale) {
        HttpEntity<GunInstallationDto> gunHttpEntity =
                Utils.createHttpEntityWithJSessionId(
                        gunInstallation,
                        RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id + "/gun", HttpMethod.POST, gunHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            ShipClassDto shipClass = shipClassService.findById(id, locale);
            model.addAttribute("add", true);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{shipClassId}/gun/{gunId}/show-update-gun-form")
    public String showUpdateGunForm(
            @PathVariable long shipClassId,
            @PathVariable long gunId,
            Model model,
            Locale locale) {
        ShipClassDto shipClass = shipClassService.findById(shipClassId, locale);
        GunInstallationDto gunInstallationDto =
                shipClassService.findGunInstallationByShipClassIdAndGunId(shipClassId, gunId);
        model.addAttribute("add", false);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunInstallation", gunInstallationDto);
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
        return "ship-class-gun-form";
    }

    @RequestMapping(value = "/{id}/gun" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateGun(
            HttpServletRequest request,
            @PathVariable long shipClassId, @PathVariable long gunId,
            @RequestBody @Valid GunInstallationDto gunInstallation,
            Model model,
            Locale locale) {
        HttpEntity<GunInstallationDto> gunHttpEntity =
                Utils.createHttpEntityWithJSessionId(
                        gunInstallation,
                        RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(
                        baseUrl + apiMapping + "/" + shipClassId + "/gun/" + gunId,
                        HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            ShipClassDto shipClass = shipClassService.findById(shipClassId, locale);
            GunDto gun = gunService.findById(gunId, locale);
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("gun", gun);
            model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

