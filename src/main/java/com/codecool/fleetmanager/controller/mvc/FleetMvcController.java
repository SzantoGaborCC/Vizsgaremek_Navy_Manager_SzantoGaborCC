package com.codecool.fleetmanager.controller.mvc;

import com.codecool.fleetmanager.DTO.FleetDTO;
import com.codecool.fleetmanager.service.FleetService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/fleet-mvc")
public class FleetMvcController {
    private final FleetService fleetService;

    public FleetMvcController(FleetService fleetService) {
        this.fleetService = fleetService;
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
    public String showCreateForm(FleetDTO fleet, Model model){
        model.addAttribute("create", true);
        return "fleet-form";
    }

    @PostMapping("/create")
    public String add(@Valid FleetDTO fleet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
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
            return "fleet-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent fleet!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @Valid FleetDTO fleet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "fleet-form";
        }
        fleetService.update(fleet, id);
        return "redirect:/fleet-mvc";
    }
}

