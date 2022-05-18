package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.DTO.OfficerDTO;
import com.codecool.navymanager.service.CountryService;
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
@RequestMapping("/officer-mvc")
public class OfficerMvcController {
    private final OfficerService officerService;
    private final RankService rankService;
    private final CountryService countryService;

    public OfficerMvcController(OfficerService officerService, RankService rankService, CountryService countryService) {
        this.officerService = officerService;
        this.rankService = rankService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listOfficers(Model model) {
        model.addAttribute("officers", officerService.findAll());
        return "officer-list";
    }

    @GetMapping("/{id}")
    public OfficerDTO findById(@PathVariable Long id) {
        return officerService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("officer", new OfficerDTO());
        model.addAttribute("create", true);
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "officer-form";
    }

    @PostMapping("/create")
    public String add(@ModelAttribute("officer") @Valid OfficerDTO officer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "officer-form";
        }
        try {
           officerService.add(officer);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid officer data!", e);
        }
        return "redirect:/officer-mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        officerService.delete(id);
        return "redirect:/officer-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id,Model model) {
        try {
            OfficerDTO officer = officerService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("officer", officer);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "officer-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship class!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("officer") @Valid OfficerDTO officer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "officer-form";
        }
        officerService.update(officer, id);
        return "redirect:/officer-mvc";
    }
}

