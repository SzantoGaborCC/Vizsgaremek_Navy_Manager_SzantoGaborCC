package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.DTO.HullClassificationDTO;
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

    @GetMapping("/{abbreviation}")
    public HullClassificationDTO findByAbbreviation(@PathVariable String abbreviation) {
        return hullClassificationService.findByAbbreviation(abbreviation);
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("create", true);
        model.addAttribute("hullClassification", new HullClassificationDTO());
        model.addAttribute("validRankValues", rankService.findAll());
        return "hull-classification-form";
    }

    @PostMapping("/create")
    public String add(@ModelAttribute("hullClassification") @Valid HullClassificationDTO hullClassification, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
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

    @GetMapping("/delete/{abbr}")
    public String deleteByAbbr(@PathVariable String abbr) {
        hullClassificationService.delete(abbr);
        return "redirect:/hull-classification-mvc";
    }

    @GetMapping("/update/{abbr}")
    public String showUpdateForm(@PathVariable String abbr, Model model) {
        try {
            HullClassificationDTO hullClassification = hullClassificationService.findByAbbreviation(abbr);
            System.out.println("found: " + hullClassification);
            model.addAttribute("create", false);
            model.addAttribute("hullClassification", hullClassification);
            model.addAttribute("validRankValues", rankService.findAll());
            return "hull-classification-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent hull classification!", e);
        }
    }

    @PostMapping("/update/{abbr}")
    public String update(@PathVariable String abbr, @ModelAttribute("hullClassification") @Valid HullClassificationDTO hullClassification, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            model.addAttribute("validRankValues", rankService.findAll());
            return "hull-classification-form";
        }
        hullClassificationService.update(hullClassification, abbr);
        return "redirect:/hull-classification-mvc";
    }
}

