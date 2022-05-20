package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.DTO.FleetDTO;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.FleetService;
import com.codecool.navymanager.service.OfficerService;
import com.codecool.navymanager.service.RankService;
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

    public FleetMvcController(FleetService fleetService, OfficerService officerService, RankService rankService, CountryService countryService) {
        this.fleetService = fleetService;
        this.officerService = officerService;
        this.rankService = rankService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listFleets(Model model) {
        model.addAttribute("fleets", fleetService.findAll());
        return "fleet-list";
    }

    @GetMapping("/{id}")
    public FleetDTO findById(@PathVariable Long id) {
        return fleetService.findById(id);
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
}

