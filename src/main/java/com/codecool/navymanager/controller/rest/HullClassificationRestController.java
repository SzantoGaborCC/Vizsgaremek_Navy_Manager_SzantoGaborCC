package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.entity.HullClassification;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.HullClassificationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.context.MessageSource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hull-classification/api")
public class HullClassificationRestController {
    private final MessageSource messageSource;

    private final HullClassificationService hullClassificationService;

    public HullClassificationRestController(MessageSource messageSource, HullClassificationService hullClassificationService) {
        this.messageSource = messageSource;
        this.hullClassificationService = hullClassificationService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns all hull classifications")
    public ResponseEntity<List<HullClassificationDto>> getAllHullClassifications() {
        return ResponseEntity.ok(hullClassificationService.findAll());
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an existing hull classification by id")
    public ResponseEntity<HullClassificationDto> getHullClassificationById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(hullClassificationService.findById(id, locale));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds a hull classification to the database")
    public ResponseEntity<JsonResponse> addHullClassification(
            @RequestBody @Valid HullClassificationDto hullClassification,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
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

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates an existing hull classification by id")
    public ResponseEntity<JsonResponse> updateHullClassification(
            @PathVariable long id,
            @RequestBody @Valid HullClassificationDto hullClassification,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
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
