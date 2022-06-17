package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.HullClassificationService;
import com.codecool.navymanager.service.RankService;
import org.apache.el.util.JreCompat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hull-classification")
public class HullClassificationController {
    private final HullClassificationService hullClassificationService;

    private final RankService rankService;

    public HullClassificationController(HullClassificationService hullClassificationService, RankService rankService) {
        this.hullClassificationService = hullClassificationService;
        this.rankService = rankService;
    }

    @GetMapping
    public String listHullClassifications(Model model) {
        model.addAttribute("hullClassifications", hullClassificationService.findAll());
        return "hull-classification-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable long id, Model model) {
        HullClassificationDto hullClassification = hullClassificationService.findById(id);
        model.addAttribute("hullClassification", hullClassification);
        return "hull-classification-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("hullClassification", new HullClassificationDto());
        model.addAttribute("validRankValues", rankService.findAll());
        return "hull-classification-form";
    }

    @PostMapping
    public ResponseEntity<JsonResponse> add(@ModelAttribute("hullClassification") @Valid HullClassificationDto hullClassification, BindingResult result, Model model) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage("Invalid hull classificaiton data!");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        hullClassificationService.add(hullClassification);
        jsonResponse.setMessage("Hull classification was added.");
        return ResponseEntity.ok().body(jsonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse> deleteById(@PathVariable long id) {
        hullClassificationService.deleteById(id);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message("Hull classification was removed.").build());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable long id, Model model) {
            HullClassificationDto hullClassification = hullClassificationService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("hullClassification", hullClassification);
            model.addAttribute("validRankValues", rankService.findAll());
            return "hull-classification-form";
    }

    //todo: when rank requirement increased, check for captain eligibility
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> update(@PathVariable long id, @ModelAttribute("hullClassification") @Valid HullClassificationDto hullClassification, BindingResult result, Model model) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage("Invalid hull classification data!");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        hullClassificationService.update(hullClassification, id);
        jsonResponse.setMessage("Hull classification was updated.");
        return ResponseEntity.ok().body(jsonResponse);
    }
}

