package com.codecool.fleetmanager.controller.mvc;

import com.codecool.fleetmanager.DTO.HullClassificationDTO;
import com.codecool.fleetmanager.service.HullClassificationService;
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
@RequestMapping("/hull-classification-mvc")
public class HullClassificationMvcController {
    private final HullClassificationService hullClassificationService;

    public HullClassificationMvcController(HullClassificationService hullClassificationService) {
        this.hullClassificationService = hullClassificationService;
    }

    @GetMapping
    public String listHullClassifications(Model model) {
        model.addAttribute("hullClassifications", hullClassificationService.findAll());
        return "hull-classification-list";
    }

    @GetMapping("/{abbreviation}")
    public HullClassificationDTO findByAbbreviation(@PathVariable String abbreviation) {
        return hullClassificationService.findByAbbreviation(abbreviation);
    }

    @GetMapping("/create")
    public String showCreateForm(HullClassificationDTO hullClassification, Model model){
        model.addAttribute("create", true);
        return "hull-classification-form";
    }

    @PostMapping("/create")
    public String add(@Valid HullClassificationDTO hullClassification, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            return "hull-classification-form";
        }
        try {
           hullClassificationService.add(hullClassification);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid hull classification data!", e);
        }
        return "redirect:/hull-classification-mvc";
    }

    @GetMapping("/delete/{abbreviation}")
    public String deleteByAbbreviation(@PathVariable String abbreviation) {
        hullClassificationService.delete(abbreviation);
        return "redirect:/hull-classification-mvc";
    }

    @GetMapping("/update/{abbreviation}")
    public String showUpdateForm(@PathVariable String abbreviation, Model model) {
        try {
            HullClassificationDTO hullClassification = hullClassificationService.findByAbbreviation(abbreviation);
            System.out.println("found: " + hullClassification);
            model.addAttribute("create", false);
            model.addAttribute("hullClassification", hullClassification);
            return "hull-classification-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent hull classification!", e);
        }
    }

    @PostMapping("/update/{abbreviation}")
    public String update(@PathVariable String abbreviation, @Valid HullClassificationDTO hullClassification, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "hull-classification-form";
        }
        hullClassificationService.update(hullClassification, abbreviation);
        return "redirect:/hull-classification-mvc";
    }
}

