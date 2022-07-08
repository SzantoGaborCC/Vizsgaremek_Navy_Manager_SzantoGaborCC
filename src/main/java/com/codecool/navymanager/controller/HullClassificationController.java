package com.codecool.navymanager.controller;

import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.entity.HullClassification;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.HullClassificationService;
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
@RequestMapping("/hull-classification")
public class HullClassificationController {
    @Autowired
    MessageSource messageSource;
    private final HullClassificationService hullClassificationService;

    private final RankService rankService;

    public HullClassificationController(HullClassificationService hullClassificationService, RankService rankService) {
        this.hullClassificationService = hullClassificationService;
        this.rankService = rankService;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model) {
        model.addAttribute("hullClassifications", hullClassificationService.findAll());
        return "hull-classification-list";
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns all hull classifications")
    public ResponseEntity<List<HullClassificationDto>> getAllHullClassifications() {
        return ResponseEntity.ok(hullClassificationService.findAll());
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, Locale locale) {
        HullClassificationDto hullClassification = hullClassificationService.findById(id, locale);
        model.addAttribute("hullClassification", hullClassification);
        return "hull-classification-details";
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns an existing hull classification by id")
    public ResponseEntity<HullClassificationDto> getHullClassificationById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(hullClassificationService.findById(id, locale));
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("hullClassification", new HullClassificationDto());
        model.addAttribute("validRankValues", rankService.findAll());
        return "hull-classification-form";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Adds a hull classification to the database")
    public ResponseEntity<JsonResponse> addHullClassification(
            @RequestBody @Valid HullClassificationDto hullClassification,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {HullClassification.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        hullClassificationService.add(hullClassification, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {HullClassification.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Deletes a hull classification by id")
    public ResponseEntity<JsonResponse> deleteHullClassificationById(@PathVariable long id, Locale locale) {
        hullClassificationService.deleteById(id, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "removed",
                new Object[] {HullClassification.class.getSimpleName()},
                locale));
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable long id, Model model, Locale locale) {
            HullClassificationDto hullClassification = hullClassificationService.findById(id, locale);
            model.addAttribute("add", false);
            model.addAttribute("hullClassification", hullClassification);
            model.addAttribute("validRankValues", rankService.findAll());
            return "hull-classification-form";
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Updates an existing hull classification by id")
    public ResponseEntity<JsonResponse> updateHullClassification(
            @PathVariable long id,
            @RequestBody @Valid HullClassificationDto hullClassification,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {HullClassification.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        hullClassificationService.update(hullClassification, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {HullClassification.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }
}

