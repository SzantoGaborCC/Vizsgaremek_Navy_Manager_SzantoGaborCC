package com.codecool.navymanager.controller;

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
@RequestMapping("/fleet")
public class FleetController {
    @Autowired
    MessageSource messageSource;
    private final FleetService fleetService;
    private final OfficerService officerService;
    private final RankService rankService;
    private final CountryService countryService;

    private final ShipService shipService;

    public FleetController(FleetService fleetService,
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

    @GetMapping("/show-list-page")
    public String showListPage(Model model) {
        model.addAttribute("fleets", fleetService.findAll());
        return "fleet-list";
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns all fleets")
    public ResponseEntity<List<FleetDto>> getAllFleets() {
        return ResponseEntity.ok(fleetService.findAll());
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, Locale locale) {
        FleetDto fleet = fleetService.findById(id, locale);
        model.addAttribute("fleet", fleet);
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-details";
    }

    @RequestMapping(value =  "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Returns an existing fleet by id")
    public ResponseEntity<FleetDto> getFleetById(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(fleetService.findById(id, locale));
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("fleet", new FleetDto());
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCommanderValues", officerService.findAvailableOfficers());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "fleet-form";
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Adds a fleet to the database")
    public ResponseEntity<JsonResponse> addFleet(
            @RequestBody @Valid FleetDto fleet,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAvailableOfficers());
            model.addAttribute("validCountryValues", countryService.findAll());
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
    @ResponseBody
    @Operation(summary = "Deletes a fleet by id")
    public ResponseEntity<JsonResponse> deleteFleetById(@PathVariable Long id, Locale locale) {
        fleetService.deleteById(id, locale);
        return ResponseEntity.ok().body(JsonResponse.builder().message(messageSource.getMessage(
                "removed",
                new Object[] {Fleet.class.getSimpleName()},
                locale)).build());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, Locale locale) {
            FleetDto fleet = fleetService.findById(id, locale);
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAvailableOfficersForFleet(fleet));
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
    }

    @RequestMapping(value = "/{id}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Updates an existing fleet by id")
    public ResponseEntity<JsonResponse> updateFleet(
            @PathVariable long id,
            @RequestBody @Valid FleetDto fleet,
            BindingResult result,
            Model model,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAvailableOfficersForFleet(fleet));
            model.addAttribute("validCountryValues", countryService.findAll());
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

    @GetMapping("/{id}/ship/show-add-ship-form")
    public String showAddShipForm(
            @PathVariable Long id,
            Model model,
            Locale locale) {
        FleetDto fleet = fleetService.findById(id, locale);
        model.addAttribute("add", true);
        model.addAttribute("fleet", fleet);
        model.addAttribute("chosenShip", new IdentityDto());
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @RequestMapping(value = "/{id}/ship" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Adds a ship to a fleet")
    public ResponseEntity<JsonResponse> addShipToFleet(
            @PathVariable Long id,
            @RequestBody @Valid IdentityDto chosenShip,
            Model model, BindingResult result,
            Locale locale) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            FleetDto fleet = fleetService.findById(id, locale);
            model.addAttribute("add", true);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
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
    @ResponseBody
    @Operation(summary = "Returns the ships of the fleet")
    public ResponseEntity<List<ShipDto>> findShipsInFleet(@PathVariable long id, Locale locale) {
        return ResponseEntity.ok(fleetService.findShipsInFleet(id, locale));
    }

    @GetMapping("/{fleetId}/ship/{shipId}/show-update-ship-form")
    public String showUpdateShipForm(
            @PathVariable long fleetId,
            @PathVariable long shipId,
            Model model,
            Locale locale) {
        FleetDto fleet = fleetService.findById(fleetId, locale);
        model.addAttribute("add", false);
        model.addAttribute("fleet", fleet);
        model.addAttribute("chosenShip", new IdentityDto(shipId));
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @RequestMapping(value = "/{fleetId}/ship/{shipId}" , method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Finds a ship in a fleet by id")
    public ResponseEntity<ShipDto> findShipInFleetById(
            @PathVariable long fleetId,
            @PathVariable  long shipId,
            Locale locale) {
        return ResponseEntity.ok(fleetService.findShipInFleet(fleetId, shipId, locale));
    }

    @RequestMapping(value = "/{fleetId}/ship/{shipId}" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Updates a ship in a fleet")
    public ResponseEntity<JsonResponse> updateShipInFleet(
            @PathVariable long fleetId, @PathVariable long shipId,
            @RequestBody @Valid IdentityDto chosenShip,
            Model model, BindingResult result,
            Locale locale) {
        if (result.hasErrors()) {
            FleetDto fleet = fleetService.findById(fleetId, locale);
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
            return ResponseEntity.badRequest()
                    .body(JsonResponse.builder().errorDescription(messageSource.getMessage(
                            "invalid_data",
                            new Object[] {Ship.class.getSimpleName()},
                            locale)).build());
        }
        fleetService.updateShipInFleet(fleetId, shipId, chosenShip.getId(), locale);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message(messageSource.getMessage(
                        "updated",
                        new Object[] {Ship.class.getSimpleName()},
                        locale)).build());
    }

    @RequestMapping(value = "/{fleetId}/ship/{shipId}" , method = RequestMethod.DELETE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    @Operation(summary = "Deletes a ship from a fleet")
    public ResponseEntity<JsonResponse> removeShipFromFleet(@PathVariable long fleetId, @PathVariable long shipId, Locale locale) {
        fleetService.removeShipFromFleet(fleetId, shipId, locale);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message(messageSource.getMessage(
                        "removed",
                        new Object[] {Ship.class.getSimpleName()},
                        locale)).build());
    }
}

