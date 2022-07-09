package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.entity.Gun;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.GunService;
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
@RequestMapping("/gun/api")
public class GunRestController {
    private final MessageSource messageSource;
    private final GunService gunService;

    public GunRestController(MessageSource messageSource, GunService gunService) {
        this.messageSource = messageSource;
        this.gunService = gunService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns all guns")
    public ResponseEntity<List<GunDto>> getAllGuns() {
        return ResponseEntity.ok(gunService.findAll());
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an existing gun by id")
    public ResponseEntity<GunDto> getGunById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(gunService.findById(id, locale));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds a gun to the database")
    public ResponseEntity<JsonResponse> addGun(
            @RequestBody @Valid GunDto gun,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Gun.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        gunService.add(gun, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {Gun.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deletes a gun by id")
    public ResponseEntity<JsonResponse> deleteGunById(@PathVariable Long id, Locale locale) {
        gunService.deleteById(id, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "removed",
                new Object[] {Gun.class.getSimpleName()},
                locale));
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates an existing gun by id")
    public ResponseEntity<JsonResponse> updateGun(
            @PathVariable long id,
            @RequestBody @Valid GunDto gun,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Gun.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        gunService.update(gun, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {Gun.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }
}
