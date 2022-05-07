package com.codecool.fleetmanager.controller.mvc;


import com.codecool.fleetmanager.model.Gun;
import com.codecool.fleetmanager.model.ShipClass;
import com.codecool.fleetmanager.service.ShipClassService;
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
@RequestMapping("/ship_class_mvc")
public class ShipClassMvcController {
    private ShipClassService shipClassService;

    public ShipClassMvcController(ShipClassService shipClassService) {
        this.shipClassService = shipClassService;
    }

    @GetMapping
    public String listShipClasses(Model model) {
        model.addAttribute("shipClasses", shipClassService.findAll());
        return "ship-class-list";
    }

    @GetMapping("/{id}")
    public ShipClass findById(@PathVariable Long id) {
        return shipClassService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(ShipClass shipClass, Model model){
        model.addAttribute("create", true);
        return "ship-form";
    }

    @PostMapping("/create")
    public String add(@Valid ShipClass shipClass, BindingResult result, Model model) {
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
        return "redirect:/ship_class_mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        shipClassService.delete(id);
        return "redirect:/ship_class_mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            ShipClass shipClass = shipClassService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("shipClass", shipClass);
            return "ship-class-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship class!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @Valid ShipClass shipClass, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "ship-class-form";
        }
        shipClassService.update(shipClass, id);
        return "redirect:/ship_class_mvc";
    }
}

