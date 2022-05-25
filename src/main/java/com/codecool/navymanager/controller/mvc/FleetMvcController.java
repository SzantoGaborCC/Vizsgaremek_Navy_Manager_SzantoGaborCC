package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.DTO.*;
import com.codecool.navymanager.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/fleet-mvc")
public class FleetMvcController {
    private final FleetService fleetService;
    private final OfficerService officerService;
    private final RankService rankService;
    private final CountryService countryService;

    private final ShipService shipService;

    public FleetMvcController(FleetService fleetService,
                              OfficerService officerService,
                              RankService rankService,
                              CountryService countryService,
                              ShipService shipService) {
        this.fleetService = fleetService;
        this.officerService = officerService;
        this.rankService = rankService;
        this.countryService = countryService;
        this.shipService = shipService;
    }

    @GetMapping
    public String listFleets(Model model) {
        model.addAttribute("fleets", fleetService.findAll());
        return "fleet-list";
    }

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        FleetDTO fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        return "fleet-details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("create", true);
        model.addAttribute("fleet", new FleetDTO());
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCommanderValues", officerService.findAll());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "fleet-form";
    }

    @PostMapping("/create")
    public String add(@ModelAttribute("fleet") @Valid FleetDTO fleet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
        }
        try {
           fleetService.add(fleet);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid fleet data!", e);
        }
        return "redirect:/fleet-mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        fleetService.delete(id);
        return "redirect:/fleet-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            FleetDTO fleet = fleetService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent fleet!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("fleet") @Valid FleetDTO fleet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
        }
        fleetService.update(fleet, id);
        return "redirect:/fleet-mvc";
    }

    @GetMapping("/add-ship/{id}")
    public String showAddShipForm(
            @PathVariable Long id,
            Model model) {
        model.addAttribute("add", true);
        FleetDTO fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        model.addAttribute("ship", new ShipDTO());
        model.addAttribute("validShipValues", shipService.findByCountryId(fleet.getCountry().getId()));
        return "fleet-ship-form";
    }

    @PostMapping("/add-ship/{id}")
    public String addGun(
            @PathVariable Long id,
            @ModelAttribute("ship") @Valid ShipDTO ship,
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            FleetDTO fleet = fleetService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findByCountryId(fleet.getCountry().getId()));
            return "fleet-ship-form";
        }
        fleetService.addShipToFleet(id, ship.getId());
        return "redirect:/fleet-mvc/details/" + id;
    }
    //todo: next is updating ship for fleet, the the many webpages for fleet ships

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

