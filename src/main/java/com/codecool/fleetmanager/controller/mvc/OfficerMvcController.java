package com.codecool.fleetmanager.controller.mvc;


import com.codecool.fleetmanager.model.Officer;
import com.codecool.fleetmanager.service.OfficerService;
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
@RequestMapping("/officer-mvc")
public class OfficerMvcController {
    private OfficerService officerService;

    public OfficerMvcController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @GetMapping
    public String listOfficers(Model model) {
        model.addAttribute("officers", officerService.findAll());
        return "officer-list";
    }

    @GetMapping("/{id}")
    public Officer findById(@PathVariable Long id) {
        return officerService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(Officer officer, Model model){
        model.addAttribute("create", true);
        return "officer-form";
    }

    @PostMapping("/create")
    public String add(@Valid Officer officer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
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
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            Officer officer = officerService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("officer", officer);
            return "officer-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship class!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @Valid Officer officer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "officer-form";
        }
        officerService.update(officer, id);
        return "redirect:/officer-mvc";
    }
}

