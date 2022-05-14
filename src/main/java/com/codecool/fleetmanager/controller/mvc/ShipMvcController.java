package com.codecool.fleetmanager.controller.mvc;


import com.codecool.fleetmanager.model.Ship;
import com.codecool.fleetmanager.service.ShipService;
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
@RequestMapping("/ship-mvc")
public class ShipMvcController {
    private ShipService shipService;

    public ShipMvcController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    public String listShips(Model model) {
        model.addAttribute("ships", shipService.findAll());
        return "ship-list";
    }

    @GetMapping("/{id}")
    public Ship findById(@PathVariable Long id) {
        return shipService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(Ship ship, Model model){
        model.addAttribute("create", true);
        return "ship-form";
    }

    @PostMapping("/create")
    public String add(@Valid Ship ship, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
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
        shipService.delete(id);
        return "redirect:/ship-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            Ship ship = shipService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("ship", ship);
            return "ship-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @Valid Ship ship, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "ship--form";
        }
        shipService.update(ship, id);
        return "redirect:/ship-mvc";
    }
}

