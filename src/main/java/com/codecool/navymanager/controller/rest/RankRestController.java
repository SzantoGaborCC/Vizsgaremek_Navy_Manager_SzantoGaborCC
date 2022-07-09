package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.dto.RankDto;
import com.codecool.navymanager.entity.Rank;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.RankService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("rank/api")
public class RankRestController {
    private final MessageSource messageSource;
    private final RankService rankService;

    public RankRestController(MessageSource messageSource, RankService rankService) {
        this.messageSource = messageSource;
        this.rankService = rankService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns all ranks")
    public ResponseEntity<List<RankDto>> getAllRanks() {
        return ResponseEntity.ok(rankService.findAll());
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an existing rank by id")
    public ResponseEntity<RankDto> getRankById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(rankService.findById(id, locale));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds a rank to the database")
    public ResponseEntity<JsonResponse> addRank(
            @RequestBody @Valid RankDto rank,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Rank.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        rankService.add(rank, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {Rank.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deletes a rank by id")
    public ResponseEntity<JsonResponse> deleteById(@PathVariable long id, Locale locale) {
        rankService.deleteById(id, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "removed",
                new Object[] {Rank.class.getSimpleName()},
                locale));
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Updates an existing rank by id")
    public ResponseEntity<JsonResponse> update(
            @PathVariable long id,
            @RequestBody @Valid RankDto rank,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
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
