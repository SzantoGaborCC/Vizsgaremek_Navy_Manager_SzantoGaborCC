package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.IdentityDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.entity.Fleet;
import com.codecool.navymanager.entity.Ship;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.*;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/fleet/api")
public class FleetRestController {
    @Autowired
    MessageSource messageSource;
    private final FleetService fleetService;
    private final OfficerService officerService;
    private final RankService rankService;
    private final CountryService countryService;

    private final ShipService shipService;

    public FleetRestController(
            FleetService fleetService,
            OfficerService officerService,
            RankService rankService,
            CountryService countryService,
            ShipService shipService) {
        this.fleetService = fleetService;
        this.officerService = officerService;
        this.rankService = rankService;
        this.countryService = countryService;
        this.shipService = shipService;
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns all fleets")
    public ResponseEntity<List<FleetDto>> getAllFleets() {
        return ResponseEntity.ok(fleetService.findAll());
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns an existing fleet by id")
    public ResponseEntity<FleetDto> getFleetById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(fleetService.findById(id, locale));
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds a fleet to the database")
    public ResponseEntity<JsonResponse> addFleet(
            @RequestBody @Valid FleetDto fleet,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Fleet.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        fleetService.add(fleet, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "added",
                new Object[] {Fleet.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deletes a fleet by id")
    public ResponseEntity<JsonResponse> deleteFleetById(@PathVariable Long id, Locale locale) {
        fleetService.deleteById(id, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "removed",
                new Object[] {Fleet.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates an existing fleet by id")
    public ResponseEntity<JsonResponse> updateFleet(
            @PathVariable long id,
            @RequestBody @Valid FleetDto fleet,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Fleet.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        fleetService.update(fleet, id, locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {Fleet.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}/ship" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Adds a ship to a fleet")
    public ResponseEntity<JsonResponse> addShipToFleet(
            @PathVariable Long id,
            @RequestBody @Valid IdentityDto chosenShip,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setErrorDescription(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Ship.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        fleetService.addShipToFleet(id, chosenShip.getId(), locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "param0_added_to_param1",
                new Object[] {Ship.class.getSimpleName(), Fleet.class.getSimpleName()},
                locale));
        return ResponseEntity.ok().body(jsonResponse);
    }

    @RequestMapping(value = "/{id}/ship" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Returns the ships of the fleet")
    public ResponseEntity<List<ShipDto>> findShipsInFleet(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(fleetService.findShipsInFleet(id, locale));
    }

    @RequestMapping(value = "/{fleetId}/ship/{shipId}" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Finds a ship in a fleet by id")
    public ResponseEntity<ShipDto> findShipInFleetById(
            @PathVariable long fleetId,
            @PathVariable  long shipId,
            Locale locale) {
        return ResponseEntity.ok(fleetService.findShipInFleet(fleetId, shipId, locale));
    }

    @RequestMapping(value = "/{fleetId}/ship/{shipId}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Updates a ship in a fleet")
    public ResponseEntity<JsonResponse> updateShipInFleet(
            @PathVariable long fleetId, @PathVariable long shipId,
            @RequestBody @Valid IdentityDto chosenShip,
            BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = new JsonResponse();
        if (result.hasErrors()) {
            jsonResponse.setMessage(messageSource.getMessage(
                    "invalid_data",
                    new Object[] {Ship.class.getSimpleName()},
                    locale));
            return ResponseEntity.badRequest()
                    .body(jsonResponse);
        }
        fleetService.updateShipInFleet(fleetId, shipId, chosenShip.getId(), locale);
        jsonResponse.setMessage(messageSource.getMessage(
                "updated",
                new Object[] {Ship.class.getSimpleName()},
                locale));
        return ResponseEntity.ok()
                .body(jsonResponse);
    }

    @RequestMapping(value = "/{fleetId}/ship/{shipId}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Deletes a ship from a fleet")
    public ResponseEntity<JsonResponse> removeShipFromFleet(@PathVariable long fleetId, @PathVariable long shipId, Locale locale) {
        fleetService.removeShipFromFleet(fleetId, shipId, locale);
        JsonResponse jsonResponse = new JsonResponse();
        jsonResponse.setMessage(messageSource.getMessage(
                "removed",
                new Object[] {Ship.class.getSimpleName()},
                locale));
        return ResponseEntity.ok()
                .body(jsonResponse);
    }
}
