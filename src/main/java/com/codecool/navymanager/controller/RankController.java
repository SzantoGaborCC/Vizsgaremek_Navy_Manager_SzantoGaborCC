package com.codecool.navymanager.controller;

import com.codecool.navymanager.dto.RankDto;
import com.codecool.navymanager.entity.Rank;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.RankService;
import com.fasterxml.jackson.annotation.JsonRootName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Locale;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rank")
public class RankController {
    @Autowired
    MessageSource messageSource;
    private final RankService rankService;

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    public String listRanks(Model model) {
        model.addAttribute("ranks", rankService.findAll());
        return "rank-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model, Locale locale) {
        RankDto rank = rankService.findById(id, locale);
        model.addAttribute("rank", rank);
        return "rank-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("rank", new RankDto());
        return "rank-form";
    }

    @PostMapping
    public ResponseEntity<JsonResponse> add(
            @RequestBody @Valid RankDto rank,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Rank.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        rankService.add(rank);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {Rank.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse> deleteById(@PathVariable long id, Locale locale) {
        rankService.deleteById(id, locale);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message(messageSource.getMessage(
                        "removed",
                        new Object[] {Rank.class.getSimpleName()},
                        locale)).build());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable long id, Model model, Locale locale) {
        RankDto rank = rankService.findById(id, locale);
        model.addAttribute("add", false);
        model.addAttribute("rank", rank);
        return "rank-form";
    }

    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> update(
            @PathVariable long id,
            @RequestBody @Valid RankDto rank,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Rank.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        rankService.update(rank, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {Rank.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }
}

