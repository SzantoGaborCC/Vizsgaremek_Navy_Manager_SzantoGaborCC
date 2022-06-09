package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.entityDTO.HullClassificationDto;
import com.codecool.navymanager.service.HullClassificationService;
import com.codecool.navymanager.service.RankService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/hull-classification-mvc")
public class HullClassificationMvcController {
    private final HullClassificationService hullClassificationService;

    private final RankService rankService;

    public HullClassificationMvcController(HullClassificationService hullClassificationService, RankService rankService) {
        this.hullClassificationService = hullClassificationService;
        this.rankService = rankService;
    }

    @GetMapping
    public String listHullClassifications(Model model) {
        model.addAttribute("hullClassifications", hullClassificationService.findAll());
        return "hull-classification-list";
    }

    @GetMapping("/{id}")
    public HullClassificationDto findById(@PathVariable long id) {
        return hullClassificationService.findById(id);
    }

    @GetMapping("/add")
    public String showCreateForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("hullClassification", new HullClassificationDto());
        model.addAttribute("validRankValues", rankService.findAll());
        return "hull-classification-form";
    }

    @PostMapping("/add")
    public String add(@ModelAttribute("hullClassification") @Valid HullClassificationDto hullClassification, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());

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

    @GetMapping("/delete/{id}")
    public String deleteByAbbr(@PathVariable long id) {
        hullClassificationService.deleteById(id);
        return "redirect:/hull-classification-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable long id, Model model) {
        try {
            HullClassificationDto hullClassification = hullClassificationService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("hullClassification", hullClassification);
            model.addAttribute("validRankValues", rankService.findAll());
            return "hull-classification-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent hull classification!", e);
        }
    }

    //todo: when rank requirement increased, check for captain eligibility
    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("hullClassification") @Valid HullClassificationDto hullClassification, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            return "hull-classification-form";
        }
        hullClassificationService.update(hullClassification, id);
        return "redirect:/hull-classification-mvc";
    }
}

