package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.OfficerService;
import com.codecool.navymanager.service.ShipClassService;
import com.codecool.navymanager.service.ShipService;
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
@RequestMapping("/ship")
public class ShipController {
    @Autowired
    MessageSource messageSource;

    private final ShipService shipService;
    private final ShipClassService shipClassService;
    private final OfficerService officerService;
    private final CountryService countryService;

    public ShipController(ShipService shipService, ShipClassService shipClassService,
                          OfficerService officerService, CountryService countryService) {
        this.shipService = shipService;
        this.shipClassService = shipClassService;
        this.officerService = officerService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listShips(Model model) {
        model.addAttribute("ships", shipService.findAll());
        return "ship-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model, Locale locale) {
        ShipDto ship = shipService.findById(id, locale);
        model.addAttribute("ship", ship);
        return "ship-details";
    }

    @GetMapping("/show-add-form")
    public String showCreateForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("ship", new ShipDto());
        model.addAttribute("validCaptainValues", officerService.findAvailableOfficers());
        model.addAttribute("validShipClassValues", shipClassService.findAll());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "ship-form";
    }

    @PostMapping
    public ResponseEntity<JsonResponse> add(
            //@ModelAttribute("ship") @Valid ShipDto ship,
            @RequestBody @Valid ShipDto ship,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validCaptainValues", officerService.findAvailableOfficers());
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Ship.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipService.add(ship, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {Ship.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse> deleteById(@PathVariable Long id, Locale locale) {
        shipService.deleteById(id, locale);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message(messageSource.getMessage(
                        "removed",
                        new Object[] {Ship.class.getSimpleName()},
                        locale)).build());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, Locale locale) {
            ShipDto ship = shipService.findById(id, locale);
            model.addAttribute("add", false);
            model.addAttribute("ship", ship);
            model.addAttribute("validCaptainValues", officerService.findAvailableOfficersForShip(ship));
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "ship-form";
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> update(
            @PathVariable long id,
            //@ModelAttribute("ship") @Valid ShipDto ship,
            @RequestBody @Valid ShipDto ship,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validCaptainValues", officerService.findAvailableOfficersForShip(ship));
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Ship.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipService.update(ship, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {Ship.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }
}

