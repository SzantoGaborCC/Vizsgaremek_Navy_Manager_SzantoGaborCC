package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.entityDTO.GunAndQuantityDto;
import com.codecool.navymanager.entityDTO.GunDto;
import com.codecool.navymanager.entityDTO.ShipClassDto;
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
        ShipClassDto shipClass = shipClassService.findById(id);
        model.addAttribute("shipClass", shipClass);
        return "ship-class-details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("create", true);
        model.addAttribute("shipClass", new ShipClassDto());
        model.addAttribute("validCountryValues", countryService.findAll());
        model.addAttribute("validHullClassificationValues", hullClassificationService.findAll());
        return "ship-class-form";
    }

    @PostMapping("/create")
    public String add(@ModelAttribute("shipClass") @Valid ShipClassDto shipClass, BindingResult result, Model model) {
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
        shipClassService.deleteById(id);
        return "redirect:/ship-class-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            ShipClassDto shipClass = shipClassService.findById(id);
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
    public String update(@PathVariable long id, @ModelAttribute("shipClass") @Valid ShipClassDto shipClass, BindingResult result, Model model) {
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
        ShipClassDto shipClass = shipClassService.findById(id);
        model.addAttribute("add", true);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunAndQuantity", new GunAndQuantityDto());
        model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
        return "ship-class-gun-form";
    }

    @PostMapping("/add-gun/{id}")
    public String addGun(
            @PathVariable Long id,
            @ModelAttribute("gunAndQuantity") @Valid GunAndQuantityDto gunAndQuantityDto,
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            ShipClassDto shipClass = shipClassService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
            return "ship-class-gun-form";
        }
        shipClassService.addGunToShipClass(id, gunAndQuantityDto);
        return "redirect:/ship-class-mvc/details/" + id;
    }

    @GetMapping("/update-gun/{shipClassId}/gun/{gunId}")
    public String showUpdateGunForm(
            @PathVariable long shipClassId, @PathVariable long gunId,
            Model model) {
        ShipClassDto shipClass = shipClassService.findById(shipClassId);
        GunAndQuantityDto gunAndQuantityDto =
                shipClassService.findGunAndQuantityByShipClassIdAndGunId(shipClassId, gunId);
        model.addAttribute("add", false);
        model.addAttribute("shipClass", shipClass);
        model.addAttribute("gunAndQuantity", gunAndQuantityDto);
        model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
        return "ship-class-gun-form";
    }

    @PostMapping("/update-gun/{shipClassId}/gun/{gunId}")
    public String updateGunForShipClass(
            @PathVariable long shipClassId, @PathVariable long gunId,
            @ModelAttribute("gunAndQuantity") @Valid GunAndQuantityDto gunAndQuantity,
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            ShipClassDto shipClass = shipClassService.findById(shipClassId);
            GunDto gun = gunService.findById(gunId);
            model.addAttribute("add", false);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("gun", gun);
            model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
            return "ship-class-gun-form";
        }
        shipClassService.updateGunForAShipClass(shipClassId, gunId,  gunAndQuantity);
        return "redirect:/ship-class-mvc/details/" + shipClassId;
    }

    @GetMapping("/delete-gun/{shipClassId}/gun/{gunId}")
    public String deleteGunFromShipClass(@PathVariable long shipClassId, @PathVariable long gunId) {
        shipClassService.deleteGunFromShipClass(shipClassId, gunId);
        return "redirect:/ship-class-mvc/details/" + shipClassId;
    }
}

