package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.OfficerService;
import com.codecool.navymanager.service.RankService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/officer")
public class OfficerController {
    private final OfficerService officerService;
    private final RankService rankService;
    private final CountryService countryService;

    public OfficerController(OfficerService officerService, RankService rankService, CountryService countryService) {
        this.officerService = officerService;
        this.rankService = rankService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listOfficers(Model model) {
        model.addAttribute("officers", officerService.findAll());
        return "officer-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        OfficerDto officer = officerService.findById(id);
        model.addAttribute("officer", officer);
        return "officer-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("officer", new OfficerDto());
        model.addAttribute("add", true);
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "officer-form";
    }

    @PostMapping
    public ResponseEntity<JsonResponse> add(@ModelAttribute("officer") @Valid OfficerDto officer, BindingResult result, Model model) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage("Invalid officer data!");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        officerService.add(officer);
        jsonResponse.setMessage("Officer was added.");
        return ResponseEntity.ok().body(jsonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse> deleteById(@PathVariable Long id) {
        officerService.deleteById(id);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message("Officer was removed.").build());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id,Model model) {
            OfficerDto officer = officerService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("officer", officer);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "officer-form";
    }

    //todo: When rank reduced, check for fleet and ship position eligibility?
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> update(@PathVariable long id, @ModelAttribute("officer") @Valid OfficerDto officer, BindingResult result, Model model) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage("Invalid hull classification data!");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        officerService.update(officer, id);
        jsonResponse.setMessage("Officer was updated.");
        return ResponseEntity.ok().body(jsonResponse);
    }
}

