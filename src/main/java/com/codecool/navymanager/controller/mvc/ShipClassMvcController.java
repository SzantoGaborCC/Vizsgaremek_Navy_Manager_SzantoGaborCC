package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.DTO.ShipClassAndGunWithQuantityDTO;
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
    public String getDetailsById(@PathVariable Long id, Model model) {
        ShipClassDTO shipClass = shipClassService.findById(id);
        model.addAttribute("shipClass", shipClass);
        return "ship-class-details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
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
    @GetMapping("/{id}/add-gun")
    public String showAddGunForm(@PathVariable Long id, Model model) {
        try {
            ShipClassDTO shipClass = shipClassService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("shipAndGunWithQuantity", new ShipClassAndGunWithQuantityDTO());
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
            return "ship-class-add-gun-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "That gun was already added!", e);
        }
    }

    @PostMapping("/{id}/add-gun")
    public String addGun(@PathVariable long id,
                         @ModelAttribute("shipClassAndGunWithQuantity") @Valid ShipClassAndGunWithQuantityDTO shipClassAndGunWithQuantity,
                         BindingResult result, Model model) {
        if (result.hasErrors()) {
            ShipClassDTO shipClass = shipClassService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("shipClass", shipClass);
            model.addAttribute("validGunValues", gunService.findByCountry(shipClass.getCountry().getId()));
            return "ship-class-add-gun-form"; //todo: create this form, it can add gun and can also update them.
        }
        shipClassService.addGunToShipClass(
                shipClassAndGunWithQuantity.getShipClassId(),
                shipClassAndGunWithQuantity.getGunId(),
                shipClassAndGunWithQuantity.getGunQuantity());
        return "redirect:/ship-class-mvc/details/" + shipClassAndGunWithQuantity.getShipClassId();
    }

    /*
    GET	    /ship-class-mvc/{id}/add-gun			show form to add gun to a ship, with the "add" attribute set to true, uses same form as update gun
    POST	/ship-class-mvc/{id}/add-gun			adds a gun to a ship, with "add" attribute set to true, uses same form as update, redirects to /ship-mvc/details/{id}

     */
}

