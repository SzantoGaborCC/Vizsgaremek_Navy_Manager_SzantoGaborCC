package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.DTO.GunDTO;
import com.codecool.navymanager.DTO.GunWithQuantityDTO;
import com.codecool.navymanager.DTO.ShipClassDTO;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.GunService;
import com.codecool.navymanager.service.HullClassificationService;
import com.codecool.navymanager.service.ShipClassService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Map;

@Controller
@RequestMapping("/ship-class-mvc")
public class ShipClassMvcController {
    private final ShipClassService shipClassService;

    private final GunService gunService;

    private final HullClassificationService hullClassificationService;
    private final CountryService countryService;

    public ShipClassMvcController(ShipClassService shipClassService, GunService gunService,
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

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        ShipClassDTO shipClass = shipClassService.findById(id);
        model.addAttribute("shipClass", shipClass);
        return "ship-class-details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("create", true);
        model.addAttribute("shipClass", new ShipClassDTO());
        model.addAttribute("validCountryValues", countryService.findAll());
        model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
        return "ship-class-form";
    }

    @PostMapping("/create")
    public String add(@ModelAttribute("shipClass") @Valid ShipClassDTO shipClass, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            return "ship-class-form";
        }
        try {
            shipClassService.add(shipClass);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid ship class data!", e);
        }
        return "redirect:/ship-class-mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        shipClassService.delete(id);
        return "redirect:/ship-class-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            ShipClassDTO shipClass = shipClassService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            return "ship-class-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship class!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("shipClass") @Valid ShipClassDTO shipClass, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            model.addAttribute("validCountryValues", countryService.findAll());
            model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
            return "ship-class-form";
        }
        shipClassService.update(shipClass, id);
        return "redirect:/ship-class-mvc";
    }

    @GetMapping("/add-gun/{id}")
    public String showAddGunForm(
            @PathVariable Long id,
            Model model) {
        model.addAttribute("add", true);
        ShipClassDTO shipClass = shipClassService.findById(id);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunWithQuantity", new GunWithQuantityDTO());
        model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
        return "ship-class-gun-form";
    }

    @PostMapping("/add-gun/{id}")
    public String addGun(
            @PathVariable Long id,
            @ModelAttribute("gunWithQuantity") @Valid GunWithQuantityDTO gunWithQuantity,
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            ShipClassDTO shipClass = shipClassService.findById(id);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
            return "ship-class-gun-form";
        }
        shipClassService.addGunToShipClass(id, gunWithQuantity.getGun().getId(), gunWithQuantity.getGunQuantity());
        return "redirect:/ship-class-mvc/details/" + id;
    }

    @GetMapping("/update-gun/{shipClassId}/gun/{gunId}")
    public String showUpdateGunForm(
            @PathVariable long shipClassId, @PathVariable long gunId,
            Model model) {
        model.addAttribute("add", false);
        ShipClassDTO shipClass = shipClassService.findById(shipClassId);
        GunWithQuantityDTO gunWithQuantity =
                shipClassService.getShipGunWithQuantityByShipClassIdAndGunId(shipClassId, gunId);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunWithQuantity", gunWithQuantity);
        model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
        return "ship-class-gun-form";
    }

    @PostMapping("/update-gun/{shipClassId}/gun/{gunId}")
    public String updateGun(
            @PathVariable long shipClassId, @PathVariable long gunId,
            @ModelAttribute("gunWithQuantity") @Valid GunWithQuantityDTO gunWithQuantity,
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            ShipClassDTO shipClass = shipClassService.findById(shipClassId);
            GunDTO gun = gunService.findById(gunId);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("gun", gun);
            model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
            return "ship-class-gun-form";
        }
        shipClassService.updateGunForAShipClass(shipClassId, gunId, gunWithQuantity.getGun().getId(), gunWithQuantity.getGunQuantity());
        return "redirect:/ship-class-mvc/details/" + shipClassId;
    }

    @GetMapping("/delete-gun/{shipClassId}/gun/{gunId}")
    public String deleteById(@PathVariable long shipClassId, @PathVariable long gunId) {
        shipClassService.deleteGunFromShipClass(shipClassId, gunId);
        return "redirect:/gun-mvc";
    }
}

