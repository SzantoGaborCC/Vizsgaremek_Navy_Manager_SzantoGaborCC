package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.dto.FleetDto;
import com.codecool.navymanager.dto.IdentityDto;
import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.response.Response;
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
    public ResponseEntity<Response> add(@ModelAttribute("fleet") @Valid FleetDto fleet, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", null);
            model.addAttribute("validCountryValues", countryService.findAll());
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid fleet data!");
            return ResponseEntity.badRequest().body(response);
        }
        fleetService.add(fleet);
        response.setMessage("Fleet was added.");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> removeShipFromFleet(@PathVariable Long id) {
        fleetService.deleteById(id);
        return ResponseEntity.ok().body("Fleet was removed.");
    }
//todo: when creating or updating ships, select options of ship classes and commanders should be based and changed upon country
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
    public ResponseEntity<Response> update(@PathVariable long id, @ModelAttribute("fleet") @Valid FleetDto fleet, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAvailableOfficersForFleet(fleet));
            model.addAttribute("validCountryValues", countryService.findAll());
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid fleet data!");
            return ResponseEntity.badRequest().body(response);
        }
        fleetService.update(fleet, id);
        response.setMessage("Fleet was updated.");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/{id}/ship/show-add-ship-form")
    public String showAddShipForm(
            @PathVariable Long id,
            Model model) {
        FleetDto fleet = fleetService.findById(id);
        model.addAttribute("add", true);
        model.addAttribute("fleet", fleet);
        model.addAttribute("chosenShip", new IdentityDto());
        //model.addAttribute("ship", new ShipDto());
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<Response> addShip(
            @PathVariable Long id,
            //@ModelAttribute("ship") @Valid ShipDto ship,
            @ModelAttribute("chosenShip") @Valid IdentityDto chosenShip,
            Model model, BindingResult result) {
        Response response = new Response();
        if (result.hasErrors()) {
            FleetDto fleet = fleetService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid ship data!");
            return ResponseEntity.badRequest().body(response);
        }
        fleetService.addShipToFleet(id, chosenShip.getId());
        response.setMessage("Ship was added to the fleet.");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{fleetId}/ship/{shipId}/show-update-ship-form")
    public String showUpdateShipForm(
            @PathVariable long fleetId, @PathVariable long shipId,
            Model model) {
        FleetDto fleet = fleetService.findById(fleetId);
        //ShipDto ship = shipService.findById(shipId);
        model.addAttribute("add", false);
        model.addAttribute("fleet", fleet);
        //model.addAttribute("ship", ship);
        model.addAttribute("chosenShip", new IdentityDto(shipId));
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @PutMapping("/{fleetId}/ship/{shipId}")
    public ResponseEntity<String> updateShipInFleet(
            @PathVariable long fleetId, @PathVariable long shipId,
            //@ModelAttribute("ship") @Valid ShipDto ship,
            @ModelAttribute("chosenShip") @Valid IdentityDto chosenShip,
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            FleetDto fleet = fleetService.findById(fleetId);
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
            return ResponseEntity.badRequest().body("Invalid ship data!");
        }

        ShipDto newShip = shipService.findAll().stream()
                .filter(shipDto -> shipDto.getId().equals(chosenShip.getId()))
                .findAny().orElse(null);
        fleetService.updateShipInAFleet(fleetId, shipId, newShip);
        return ResponseEntity.ok().body("Ship in fleet was updated.");
    }

    @DeleteMapping("/{fleetId}/ship/{shipId}")
    public ResponseEntity<String> removeShipFromFleet(@PathVariable long fleetId, @PathVariable long shipId) {
        fleetService.removeShipFromFleet(fleetId, shipId);
        return ResponseEntity.ok().body("Ship was removed from the fleet.");
    }
}

