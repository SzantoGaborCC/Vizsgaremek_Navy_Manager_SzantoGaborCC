package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.dto.GunInstallationDto;
import com.codecool.navymanager.dto.ShipClassDto;
import com.codecool.navymanager.entity.Gun;
import com.codecool.navymanager.entity.ShipClass;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.GunService;
import com.codecool.navymanager.service.HullClassificationService;
import com.codecool.navymanager.service.ShipClassService;
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
@RequestMapping("/ship-class")
public class ShipClassController {
    @Autowired
    MessageSource messageSource;
    private final ShipClassService shipClassService;

    private final GunService gunService;

    private final HullClassificationService hullClassificationService;
    private final CountryService countryService;

    public ShipClassController(ShipClassService shipClassService, GunService gunService,
                               HullClassificationService hullClassificationService, CountryService countryService) {
        this.shipClassService = shipClassService;
        this.gunService = gunService;
        this.hullClassificationService = hullClassificationService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listShipClasses(Model model) {
        model.addAttribute("shipClasses", shipClassService.findAll());
        return "ship-class-list";
    }

    @GetMapping("/{id}")
    public String showDetails(@PathVariable Long id, Model model, Locale locale) {
        ShipClassDto shipClass = shipClassService.findById(id);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
        return "ship-class-details";
    }

    @GetMapping("/show-add-form")
    public String showCreateForm(Model model) {
        model.addAttribute("add", true);
        model.addAttribute("shipClass", new ShipClassDto());
        model.addAttribute("validCountryValues", countryService.findAll());
        model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
        return "ship-class-form";
    }

    @PostMapping
    public ResponseEntity<JsonResponse> add(
            @ModelAttribute("shipClass") @Valid ShipClassDto shipClass,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {ShipClass.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipClassService.add(shipClass);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id, Locale locale) {
        shipClassService.deleteById(id, locale);
        return ResponseEntity.ok().body(messageSource.getMessage(
                "removed",
                new Object[] {ShipClass.class.getSimpleName()},
                locale));
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model) {
            ShipClassDto shipClass = shipClassService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            return "ship-class-form";
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> update(
            @PathVariable long id,
            @ModelAttribute("shipClass") @Valid ShipClassDto shipClass,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {ShipClass.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipClassService.update(shipClass, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }
    //todo: adding the same gun should impossible, should check for ship displacement.
    // When ship displacement reduced, check for gun removal?
    @GetMapping("/{id}/gun/show-add-gun-form")
    public String showAddGunForm(
            @PathVariable Long id,
            Model model,
            Locale locale) {
        ShipClassDto shipClass = shipClassService.findById(id);
        model.addAttribute("add", true);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunAndQuantity", new GunInstallationDto());
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
        return "ship-class-gun-form";
    }

    @PostMapping("/{id}/gun")
    public ResponseEntity<JsonResponse> addGun(
            @PathVariable Long id,
            @ModelAttribute("gunAndQuantity") @Valid GunInstallationDto gunInstallationDto,
            Model model,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            ShipClassDto shipClass = shipClassService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Gun.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipClassService.addGunToShipClass(id, gunInstallationDto, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "param0_added_to_param1",
                new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @GetMapping("{shipClassId}/gun/{gunId}/show-update-gun-form")
    public String showUpdateGunForm(
            @PathVariable long shipClassId,
            @PathVariable long gunId,
            Model model,
            Locale locale) {
        ShipClassDto shipClass = shipClassService.findById(shipClassId);
        GunInstallationDto gunInstallationDto =
                shipClassService.findGunAndQuantityByShipClassIdAndGunId(shipClassId, gunId);
        model.addAttribute("add", false);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunAndQuantity", gunInstallationDto);
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
        return "ship-class-gun-form";
    }

    @PutMapping("/{shipClassId}/gun/{gunId}")
    public ResponseEntity<JsonResponse> updateGunForShipClass(
            @PathVariable long shipClassId, @PathVariable long gunId,
            @ModelAttribute("gunAndQuantity") @Valid GunInstallationDto gunAndQuantity,
            Model model,
            BindingResult result,
            Locale locale) {
        if (result.hasErrors()) {
            ShipClassDto shipClass = shipClassService.findById(shipClassId);
            GunDto gun = gunService.findById(gunId, locale);
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("gun", gun);
            model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
            return ResponseEntity.badRequest()
                    .body(JsonResponse.builder().message(messageSource.getMessage(
                            "invalid_data",
                            new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                            locale)).build());
        }
        shipClassService.updateGunForShipClass(shipClassId, gunId,  gunAndQuantity, locale);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message(messageSource.getMessage(
                        "updated",
                        new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                        locale)).build());
    }

    @DeleteMapping("/{shipClassId}/gun/{gunId}")
    public ResponseEntity<JsonResponse> removeGunFromShipClass(
            @PathVariable long shipClassId,
            @PathVariable long gunId,
            Locale locale) {
        shipClassService.removeGunFromShipClass(shipClassId, gunId, locale);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message(messageSource.getMessage(
                        "removed",
                        new Object[] {Gun.class.getSimpleName()},
                        locale)).build());
    }
}

