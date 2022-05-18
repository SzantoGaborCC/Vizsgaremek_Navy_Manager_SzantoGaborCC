package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.DTO.ShipClassDTO;
import com.codecool.navymanager.service.CountryService;
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

    private final HullClassificationService hullClassificationService;
    private final CountryService countryService;

    public ShipClassMvcController(ShipClassService shipClassService, HullClassificationService hullClassificationService, CountryService countryService) {
        this.shipClassService = shipClassService;
        this.hullClassificationService = hullClassificationService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listShipClasses(Model model) {
        model.addAttribute("shipClasses", shipClassService.findAll());
        return "ship-class-list";
    }

    @GetMapping("/{id}")
    public ShipClassDTO findById(@PathVariable Long id) {
        return shipClassService.findById(id);
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
}

