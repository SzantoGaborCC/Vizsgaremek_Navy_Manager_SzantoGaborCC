package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.DTO.ShipClassDTO;
import com.codecool.navymanager.service.ShipClassService;
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
@RequestMapping("/ship-class-mvc")
public class ShipClassMvcController {
    private final ShipClassService shipClassService;

    public ShipClassMvcController(ShipClassService shipClassService) {
        this.shipClassService = shipClassService;
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
    public String showCreateForm(ShipClassDTO shipClass, Model model){
        model.addAttribute("create", true);
        return "ship-class-form";
    }

    @PostMapping("/create")
    public String add(@Valid ShipClassDTO shipClass, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
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
            return "ship-class-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship class!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @Valid ShipClassDTO shipClass, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "ship-class-form";
        }
        shipClassService.update(shipClass, id);
        return "redirect:/ship-class-mvc";
    }
}

