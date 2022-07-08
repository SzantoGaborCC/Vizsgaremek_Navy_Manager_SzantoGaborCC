package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.OfficerService;
import com.codecool.navymanager.service.RankService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/officer")
public class OfficerController {
    @Autowired
    MessageSource messageSource;

    private final OfficerService officerService;
    private final RankService rankService;
    private final CountryService countryService;

    public OfficerController(OfficerService officerService, RankService rankService, CountryService countryService) {
        this.officerService = officerService;
        this.rankService = rankService;
        this.countryService = countryService;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model) {
        model.addAttribute("officers", officerService.findAll());
        return "officer-list";
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns all officers")
    public ResponseEntity<List<OfficerDto>> getAllOfficers() {
        return ResponseEntity.ok(officerService.findAll());
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, Locale locale) {
        OfficerDto officer = officerService.findById(id, locale);
        model.addAttribute("officer", officer);
        return "officer-details";
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns an existing officer by id")
    public ResponseEntity<OfficerDto> getOfficerById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(officerService.findById(id, locale));
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("officer", new OfficerDto());
        model.addAttribute("add", true);
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "officer-form";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Adds an officer to the database")
    public ResponseEntity<JsonResponse> addOfficer(
            @RequestBody @Valid OfficerDto officer,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Officer.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        officerService.add(officer, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {Officer.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Deletes an officer by id")
    public ResponseEntity<JsonResponse> deleteOfficerById(@PathVariable Long id, Locale locale) {
        officerService.deleteById(id, locale);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message(messageSource.getMessage(
                        "removed",
                        new Object[] {Officer.class.getSimpleName()},
                        locale)).build());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id,Model model, Locale locale) {
            OfficerDto officer = officerService.findById(id, locale);
            model.addAttribute("add", false);
            model.addAttribute("officer", officer);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "officer-form";
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Updates an existing officer by id")
    public ResponseEntity<JsonResponse> updateOfficer(
            @PathVariable long id,
            @RequestBody @Valid OfficerDto officer,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Officer.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        officerService.update(officer, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {Officer.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }
}

