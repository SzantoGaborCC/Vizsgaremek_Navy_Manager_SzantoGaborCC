package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.dto.GunInstallationDto;
import com.codecool.navymanager.dto.ShipClassDto;
import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Gun;
import com.codecool.navymanager.entity.ShipClass;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.GunService;
import com.codecool.navymanager.service.HullClassificationService;
import com.codecool.navymanager.service.ShipClassService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
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

    @GetMapping("/show-list-page")
    public String showListPage(Model model) {
        model.addAttribute("shipClasses", shipClassService.findAll());
        return "ship-class-list";
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns all ship classes")
    public ResponseEntity<List<ShipClassDto>> getAllShipClasses() {
        return ResponseEntity.ok(shipClassService.findAll());
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, Locale locale) {
        ShipClassDto shipClass = shipClassService.findById(id, locale);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
        return "ship-class-details";
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns an existing ship class by id")
    public ResponseEntity<ShipClassDto> getShipClassById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(shipClassService.findById(id, locale));
    }

    @GetMapping("/show-add-form")
    public String showCreateForm(Model model) {
        model.addAttribute("add", true);
        model.addAttribute("shipClass", new ShipClassDto());
        model.addAttribute("validCountryValues", countryService.findAll());
        model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
        return "ship-class-form";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Adds a ship class to the database")
    public ResponseEntity<JsonResponse> addShipClass(
            @RequestBody @Valid ShipClassDto shipClass,
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
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {ShipClass.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipClassService.add(shipClass, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Deletes a ship class by id")
    public ResponseEntity<JsonResponse> deleteById(@PathVariable Long id, Locale locale) {
        shipClassService.deleteById(id, locale);
        return ResponseEntity.ok().body(JsonResponse.builder().message(messageSource.getMessage(
                "removed",
                new Object[] {Fleet.class.getSimpleName()},
                locale)).build());
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

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Updates an existing ship class by id")
    public ResponseEntity<JsonResponse> update(
            @PathVariable long id,
            @RequestBody @Valid ShipClassDto shipClass,
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
            jsonResponse.setErrorDescription(messageSource.getMessage(
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
    @ResponseBody
    @Operation(summary = "Adds a gun to a ship class")
    public ResponseEntity<JsonResponse> addGun(
            @PathVariable Long id,
            @RequestBody @Valid GunInstallationDto gunInstallation,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            ShipClassDto shipClass = shipClassService.findById(id, locale);
            model.addAttribute("add", true);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Gun.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipClassService.addGunToShipClass(id, gunInstallation, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "param0_added_to_param1",
                new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}/gun" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns the guns of a ship class")
    public ResponseEntity<List<GunInstallationDto>> findGuns(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(shipClassService.findGuns(id, locale));
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

    @GetMapping("/{shipClassId}/gun/{gunId}")
    @RequestMapping(value = "/{shipClassId}/gun/{gunId}" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Finds a gun in a ship class")
    public ResponseEntity<GunInstallationDto> findGunInShipClassById(
            @PathVariable long shipClassId,
            @PathVariable  long gunId,
            Locale locale) {
        return ResponseEntity.ok(shipClassService.findGunInShipClassById(shipClassId, gunId, locale));
    }

    @RequestMapping(value = "/{shipClassId}/gun/{gunId}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Updates a gun in a ship class")
    public ResponseEntity<JsonResponse> updateGunForShipClass(
            @PathVariable long shipClassId, @PathVariable long gunId,
            @RequestBody @Valid GunInstallationDto gunInstallation,
            BindingResult result,
            Model model,
            Locale locale) {
        if (result.hasErrors()) {
            ShipClassDto shipClass = shipClassService.findById(shipClassId, locale);
            GunDto gun = gunService.findById(gunId, locale);
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("gun", gun);
            model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass, locale));
            return ResponseEntity.badRequest()
                    .body(JsonResponse.builder().errorDescription(messageSource.getMessage(
                            "invalid_data",
                            new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                            locale)).build());
        }
        shipClassService.updateGunForShipClass(shipClassId, gunId,  gunInstallation, locale);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message(messageSource.getMessage(
                        "updated",
                        new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                        locale)).build());
    }

    @RequestMapping(value = "/{shipClassId}/gun/{gunId}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Deletes a gun from a ship class")
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

