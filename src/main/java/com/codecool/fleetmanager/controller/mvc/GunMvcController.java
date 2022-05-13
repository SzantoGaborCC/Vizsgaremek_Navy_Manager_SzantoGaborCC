package com.codecool.fleetmanager.controller.mvc;

import com.codecool.fleetmanager.model.Gun;
import com.codecool.fleetmanager.service.GunService;
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
@RequestMapping("/gun-mvc")
public class GunMvcController {
    private GunService gunService;

    public GunMvcController(GunService gunService) {
        this.gunService = gunService;
    }

    @GetMapping
    public String listGuns(Model model) {
        model.addAttribute("guns", gunService.findAll());
        return "gun-list";
    }

    @GetMapping("/{id}")
    public Gun findById(@PathVariable Long id) {
        return gunService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(Gun gun, Model model){
        model.addAttribute("create", true);
        return "gun-form";
    }

    @PostMapping("/create")
    public String add(@Valid Gun gun, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            return "gun-form";
        }
        try {
           gunService.add(gun);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid gun data!", e);
        }
        return "redirect:/gun_mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        gunService.delete(id);
        return "redirect:/gun_mvc";
    }

    @GetMapping("/form/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
           Gun gun = gunService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("gun", gun);
            return "gun-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent gun!", e);
        }
    }

    @PostMapping("/form/{id}")
    public String update(@PathVariable long id, @Valid Gun gun, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "gun-form";
        }
        gunService.update(gun, id);
        return "redirect:/gun_mvc";
    }
}

