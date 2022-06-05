package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.entityDTO.ShipDto;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.OfficerService;
import com.codecool.navymanager.service.ShipClassService;
import com.codecool.navymanager.service.ShipService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/ship-mvc")
public class ShipMvcController {
    private final ShipService shipService;
    private final ShipClassService shipClassService;
    private final OfficerService officerService;
    private final CountryService countryService;

    public ShipMvcController(ShipService shipService, ShipClassService shipClassService,
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
    public ShipDto findById(@PathVariable Long id) {
        return shipService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("create", true);
        model.addAttribute("ship", new ShipDto());
        model.addAttribute("validCaptainValues", null);
        model.addAttribute("validShipClassValues", shipClassService.findAll());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "ship-form";
    }

    @PostMapping("/create")
    public String add(@ModelAttribute("ship") @Valid ShipDto ship, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            model.addAttribute("validCaptainValues", null);
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "ship-form";
        }
        try {
           shipService.add(ship);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid ship data!", e);
        }
        return "redirect:/ship-mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        shipService.deleteById(id);
        return "redirect:/ship-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            ShipDto ship = shipService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("ship", ship);
            model.addAttribute("validCaptainValues", officerService.findAvailableOfficersByCountry(ship.getCountry()));
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "ship-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("ship") @Valid ShipDto ship, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            model.addAttribute("validCaptainValues", officerService.findAvailableOfficersByCountry(ship.getCountry()));
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "ship-form";
        }
        shipService.update(ship, id);
        return "redirect:/ship-mvc";
    }
}

