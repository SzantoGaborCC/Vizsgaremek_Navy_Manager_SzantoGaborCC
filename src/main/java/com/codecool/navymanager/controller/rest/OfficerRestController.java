package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.entity.Officer;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.OfficerService;
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
@RequestMapping("officer/api")
public class OfficerRestController {

    private final MessageSource messageSource;

    private final OfficerService officerService;

    public OfficerRestController(MessageSource messageSource, OfficerService officerService) {
        this.messageSource = messageSource;
        this.officerService = officerService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns all officers")
    public ResponseEntity<List<OfficerDto>> getAllOfficers() {
        return ResponseEntity.ok(officerService.findAll());
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an existing officer by id")
    public ResponseEntity<OfficerDto> getOfficerById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(officerService.findById(id, locale));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds an officer to the database")
    public ResponseEntity<JsonResponse> addOfficer(
            @RequestBody @Valid OfficerDto officer,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
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
    @Operation(summary = "Deletes an officer by id")
    public ResponseEntity<JsonResponse> deleteOfficerById(@PathVariable Long id, Locale locale) {
        officerService.deleteById(id, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "removed",
                new Object[] {Officer.class.getSimpleName()},
                locale));
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates an existing officer by id")
    public ResponseEntity<JsonResponse> updateOfficer(
            @PathVariable long id,
            @RequestBody @Valid OfficerDto officer,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
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

    @RequestMapping(value = "/available" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds all available officers")
    public List<OfficerDto> findAvailableOfficers() {
        return officerService.findAvailableOfficers();
    }

    @RequestMapping(value = "/available/country/{countryId}" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds all available officers for a country")
    public List<OfficerDto> findAvailableOfficersByCountry(@PathVariable long countryId) {
        return officerService.findAvailableOfficersByCountry(countryId);
    }

    @RequestMapping(value = "/available/ship/{shipId}" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds all available officers for a ship")
    public List<OfficerDto> findAvailableOfficersForShip(@PathVariable long shipId, Locale locale) {
        return officerService.findAvailableOfficersForShip(shipId, locale);
    }

    @RequestMapping(value = "/available/fleet/{fleetId}" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds all available officers for a fleet")
    public List<OfficerDto> findAvailableOfficersForFleet(@PathVariable long fleetId, Locale locale) {
        return officerService.findAvailableOfficersForFleet(fleetId, locale);
    }
}
