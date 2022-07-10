package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.dto.GunInstallationDto;
import com.codecool.navymanager.dto.ShipClassDto;
import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Gun;
import com.codecool.navymanager.entity.ShipClass;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.ShipClassService;
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
@RequestMapping("/ship-class/api")
public class ShipClassRestController {
    private final MessageSource messageSource;
    private final ShipClassService shipClassService;

    public ShipClassRestController(MessageSource messageSource, ShipClassService shipClassService) {
        this.messageSource = messageSource;
        this.shipClassService = shipClassService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns all ship classes")
    public ResponseEntity<List<ShipClassDto>> getAllShipClasses() {
        return ResponseEntity.ok(shipClassService.findAll());
    }

    @RequestMapping(value = "/{id}/gun/available" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns available guns for ship the class")
    public ResponseEntity<List<GunDto>> findValidGuns(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(shipClassService.findValidGuns(id, locale));
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an existing ship class by id")
    public ResponseEntity<ShipClassDto> getShipClassById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(shipClassService.findById(id, locale));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds a ship class to the database")
    public ResponseEntity<JsonResponse> addShipClass(
            @RequestBody @Valid ShipClassDto shipClass,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {ShipClass.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipClassService.add(shipClass, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deletes a ship class by id")
    public ResponseEntity<JsonResponse> deleteById(@PathVariable Long id, Locale locale) {
        shipClassService.deleteById(id, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "removed",
                new Object[] {Fleet.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates an existing ship class by id")
    public ResponseEntity<JsonResponse> update(
            @PathVariable long id,
            @RequestBody @Valid ShipClassDto shipClass,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {ShipClass.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        shipClassService.update(shipClass, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}/gun" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds a gun to a ship class")
    public ResponseEntity<JsonResponse> addGun(
            @PathVariable Long id,
            @RequestBody @Valid GunInstallationDto gunInstallation,
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
        shipClassService.addGunToShipClass(id, gunInstallation, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "param0_added_to_param1",
                new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}/gun" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns the guns of a ship class")
    public ResponseEntity<List<GunInstallationDto>> findGuns(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(shipClassService.findGuns(id, locale));
    }

    @RequestMapping(value = "/{shipClassId}/gun/{gunId}" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds a gun in a ship class")
    public ResponseEntity<GunInstallationDto> findGunInShipClassById(
            @PathVariable long shipClassId,
            @PathVariable  long gunId,
            Locale locale) {
        return ResponseEntity.ok(shipClassService.findGunInShipClassById(shipClassId, gunId, locale));
    }

    @RequestMapping(value = "/{shipClassId}/gun/{gunId}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a gun in a ship class")
    public ResponseEntity<JsonResponse> updateGunForShipClass(
            @PathVariable long shipClassId, @PathVariable long gunId,
            @RequestBody @Valid GunInstallationDto gunInstallation,
            BindingResult result,
            Locale locale) {
        if (result.hasErrors()) {
            JsonResponse jsonResponse = new JsonResponse();
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest()
                    .body(jsonResponse);
        }
        shipClassService.updateGunForShipClass(shipClassId, gunId,  gunInstallation, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {Gun.class.getSimpleName(), ShipClass.class.getSimpleName()},
                locale));
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    @RequestMapping(value = "/{shipClassId}/gun/{gunId}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deletes a gun from a ship class")
    public ResponseEntity<JsonResponse> removeGunFromShipClass(
            @PathVariable long shipClassId,
            @PathVariable long gunId,
            Locale locale) {
        shipClassService.removeGunFromShipClass(shipClassId, gunId, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "removed",
                new Object[] {Gun.class.getSimpleName()},
                locale));
        return ResponseEntity.ok()
                .body(jsonResponse);
    }
}
