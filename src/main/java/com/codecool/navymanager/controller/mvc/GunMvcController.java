package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.entityDTO.GunDto;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.GunService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/gun-mvc")
public class GunMvcController {
    private final GunService gunService;
    private final CountryService countryService;

    public GunMvcController(GunService gunService, CountryService countryService) {
        this.gunService = gunService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listGuns(Model model) {
        model.addAttribute("guns", gunService.findAll());
        return "gun-list";
    }

    @GetMapping("/{id}")
    public GunDto findById(@PathVariable Long id) {
        return gunService.findById(id);
    }

    @GetMapping("/add")
    public String showCreateForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("gun", new GunDto());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "gun-form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("gun") @Valid GunDto gun, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validCountryValues", countryService.findAll());
            return "gun-form";
        }
        try {
           gunService.add(gun);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid gun data!", e);
        }
        return "redirect:/gun-mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        gunService.deleteById(id);
        return "redirect:/gun-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
           GunDto gun = gunService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("gun", gun);
            model.addAttribute("validCountryValues", countryService.findAll());
            return "gun-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent gun!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("gun") @Valid GunDto gun, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validCountryValues", countryService.findAll());
            return "gun-form";
        }
        gunService.update(gun, id);
        return "redirect:/gun-mvc";
    }
}

