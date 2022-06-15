package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.entityDTO.GunAndQuantityDto;
import com.codecool.navymanager.entityDTO.GunDto;
import com.codecool.navymanager.entityDTO.ShipClassDto;
import com.codecool.navymanager.response.Response;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.GunService;
import com.codecool.navymanager.service.HullClassificationService;
import com.codecool.navymanager.service.ShipClassService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/ship-class")
public class ShipClassController {
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
    public String showDetails(@PathVariable Long id, Model model) {
        ShipClassDto shipClass = shipClassService.findById(id);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass));
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
    public ResponseEntity<Response> add(@ModelAttribute("shipClass") @Valid ShipClassDto shipClass, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid ship class data!");
            return ResponseEntity.badRequest().body(response);
        }
        try {
            shipClassService.add(shipClass);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid ship class data!", e);
        }
        response.setMessage("Ship class was added.");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        shipClassService.deleteById(id);
        return ResponseEntity.ok().body("Fleet was removed.");
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
    public ResponseEntity<Response> update(@PathVariable long id, @ModelAttribute("shipClass") @Valid ShipClassDto shipClass, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid ship class data!");
            return ResponseEntity.badRequest().body(response);
        }
        shipClassService.update(shipClass, id);
        response.setMessage("Ship class was updated.");
        return ResponseEntity.ok().body(response);
    }
    //todo: adding the same gun should impossible, should check for ship displacement.
    // When ship displacement reduced, check for gun removal?
    @GetMapping("/{id}/gun/show-add-gun-form")
    public String showAddGunForm(
            @PathVariable Long id,
            Model model) {
        ShipClassDto shipClass = shipClassService.findById(id);
        model.addAttribute("add", true);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunAndQuantity", new GunAndQuantityDto());
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass));
        return "ship-class-gun-form";
    }

    @PostMapping("/{id}/gun")
    public ResponseEntity<Response> addGun(
            @PathVariable Long id,
            @ModelAttribute("gunAndQuantity") @Valid GunAndQuantityDto gunAndQuantityDto,
            Model model, BindingResult result) {
        Response response = new Response();
        if (result.hasErrors()) {
            ShipClassDto shipClass = shipClassService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass));
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid gun data!");
            return ResponseEntity.badRequest().body(response);
        }
        shipClassService.addGunToShipClass(id, gunAndQuantityDto);
        response.setMessage("Gun was added to the ship class.");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{shipClassId}/gun/{gunId}/show-update-gun-form")
    public String showUpdateGunForm(
            @PathVariable long shipClassId, @PathVariable long gunId,
            Model model) {
        ShipClassDto shipClass = shipClassService.findById(shipClassId);
        GunAndQuantityDto gunAndQuantityDto =
                shipClassService.findGunAndQuantityByShipClassIdAndGunId(shipClassId, gunId);
        model.addAttribute("add", false);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunAndQuantity", gunAndQuantityDto);
        model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass));
        return "ship-class-gun-form";
    }

    @PutMapping("/{shipClassId}/gun/{gunId}")
    public ResponseEntity<String> updateGunForShipClass(
            @PathVariable long shipClassId, @PathVariable long gunId,
            @ModelAttribute("gunAndQuantity") @Valid GunAndQuantityDto gunAndQuantity,
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            ShipClassDto shipClass = shipClassService.findById(shipClassId);
            GunDto gun = gunService.findById(gunId);
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("gun", gun);
            model.addAttribute("validGunValues", shipClassService.findValidGuns(shipClass));
            return ResponseEntity.badRequest().body("Invalid gun data!");
        }
        shipClassService.updateGunForShipClass(shipClassId, gunId,  gunAndQuantity);
        return ResponseEntity.ok().body("Gun for ship class was updated.");
    }

    @DeleteMapping("/{shipClassId}/gun/{gunId}")
    public ResponseEntity<String> removeGunFromShipClass(@PathVariable long shipClassId, @PathVariable long gunId) {
        shipClassService.removeGunFromShipClass(shipClassId, gunId);
        return ResponseEntity.ok().body("Gun was removed from the ship class.");
    }
}

