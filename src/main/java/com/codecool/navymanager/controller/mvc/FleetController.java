package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.IdentityDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/fleet")
public class FleetController {
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

    @GetMapping
    public String listFleets(Model model) {
        model.addAttribute("fleets", fleetService.findAll());
        return "fleet-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        FleetDto fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("fleet", new FleetDto());
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCommanderValues", null);
        model.addAttribute("validCountryValues", countryService.findAll());
        return "fleet-form";
    }

    @PostMapping
    public ResponseEntity<JsonResponse> add(@ModelAttribute("fleet") @Valid FleetDto fleet, BindingResult result, Model model) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", null);
            model.addAttribute("validCountryValues", countryService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage("Invalid fleet data!");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        fleetService.add(fleet);
        jsonResponse.setMessage("Fleet was added.");
        return ResponseEntity.ok().body(jsonResponse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<JsonResponse> removeShipFromFleet(@PathVariable Long id) {
        fleetService.deleteById(id);
        return ResponseEntity.ok().body(JsonResponse.builder().message("Fleet was removed.").build());
    }
//todo: when creating or updating ships ajax should be used, select options of ship classes and commanders should be based and changed upon country
    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model) {
            FleetDto fleet = fleetService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAvailableOfficersForFleet(fleet));
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
    }

    //todo: when rank requirement increased, check for commander eligibility, possibly removing him
    @PutMapping("/{id}")
    public ResponseEntity<JsonResponse> update(@PathVariable long id, @ModelAttribute("fleet") @Valid FleetDto fleet, BindingResult result, Model model) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAvailableOfficersForFleet(fleet));
            model.addAttribute("validCountryValues", countryService.findAll());
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage("Invalid fleet data!");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        fleetService.update(fleet, id);
        jsonResponse.setMessage("Fleet was updated.");
        return ResponseEntity.ok().body(jsonResponse);
    }

    @GetMapping("/{id}/ship/show-add-ship-form")
    public String showAddShipForm(
            @PathVariable Long id,
            Model model) {
        FleetDto fleet = fleetService.findById(id);
        model.addAttribute("add", true);
        model.addAttribute("fleet", fleet);
        model.addAttribute("chosenShip", new IdentityDto());
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<JsonResponse> addShip(
            @PathVariable Long id,
            @ModelAttribute("chosenShip") @Valid IdentityDto chosenShip,
            Model model, BindingResult result) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            FleetDto fleet = fleetService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
            jsonResponse.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            jsonResponse.setMessage("Invalid ship data!");
            return ResponseEntity.badRequest().body(jsonResponse);
        }
        fleetService.addShipToFleet(id, chosenShip.getId());
        jsonResponse.setMessage("Ship was added to the fleet.");
        return ResponseEntity.ok().body(jsonResponse);
    }

    @GetMapping("{fleetId}/ship/{shipId}/show-update-ship-form")
    public String showUpdateShipForm(
            @PathVariable long fleetId, @PathVariable long shipId,
            Model model) {
        FleetDto fleet = fleetService.findById(fleetId);
        model.addAttribute("add", false);
        model.addAttribute("fleet", fleet);
        model.addAttribute("chosenShip", new IdentityDto(shipId));
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @PutMapping("/{fleetId}/ship/{shipId}")
    public ResponseEntity<JsonResponse> updateShipInFleet(
            @PathVariable long fleetId, @PathVariable long shipId,
            @ModelAttribute("chosenShip") @Valid IdentityDto chosenShip,
            Model model, BindingResult result) {
        JsonResponse jsonResponse = JsonResponse.builder().build();
        if (result.hasErrors()) {
            FleetDto fleet = fleetService.findById(fleetId);
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
            return ResponseEntity.badRequest()
                    .body(JsonResponse.builder().message("Invalid ship data!").build());
        }
        fleetService.updateShipInAFleet(fleetId, shipId, chosenShip.getId());
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message("Ship updated.").build());
    }

    @DeleteMapping("/{fleetId}/ship/{shipId}")
    public ResponseEntity<JsonResponse> removeShipFromFleet(@PathVariable long fleetId, @PathVariable long shipId) {
        fleetService.removeShipFromFleet(fleetId, shipId);
        return ResponseEntity.ok()
                .body(JsonResponse.builder().message("Ship removed from fleet.").build());
    }
}

