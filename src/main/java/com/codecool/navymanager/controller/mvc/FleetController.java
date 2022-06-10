package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.entityDTO.FleetDto;
import com.codecool.navymanager.entityDTO.ShipDto;
import com.codecool.navymanager.response.Response;
import com.codecool.navymanager.service.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

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
    public String showDetails(@PathVariable Long id, Model model) {
        FleetDto fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-details";
    }
//todo: working on fleet details
    @GetMapping("/add")
    public String showCreateForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("fleet", new FleetDto());
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCommanderValues", null);
        model.addAttribute("validCountryValues", countryService.findAll());
        return "fleet-form";
    }

    @PostMapping()
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
        HttpHeaders headers = new HttpHeaders();
        System.out.println("using headers!");
        headers.add("Location", "/fleet");
        return ResponseEntity.ok().headers(headers).body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        fleetService.deleteById(id);
        return ResponseEntity.ok().body("Fleet removed.");
    }

    @GetMapping("/{id}/update")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            FleetDto fleet = fleetService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAvailableOfficersForFleet(fleet));
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent fleet!", e);
        }
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

    @GetMapping("/{id}/ship/add")
    public String showAddShipForm(
            @PathVariable Long id,
            Model model) {
        model.addAttribute("add", true);
        FleetDto fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        model.addAttribute("ship", new ShipDto());//
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @PostMapping("/{id}/ship")
    public ResponseEntity<Response> addShip(
            @PathVariable Long id,
            @ModelAttribute("ship") @Valid ShipDto ship,//
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
        fleetService.addShipToFleet(id, ship);
        response.setMessage("Ship was added to the fleet.");
        return ResponseEntity.ok().body(response);
    }

    @GetMapping("{fleetId}/ship/{shipId}/update")
    public String showUpdateShipForm(
            @PathVariable long fleetId, @PathVariable long shipId,
            Model model) {
        FleetDto fleet = fleetService.findById(fleetId);
        ShipDto ship = shipService.findById(shipId);
        model.addAttribute("add", false);
        model.addAttribute("fleet", fleet);
        model.addAttribute("ship", ship);//
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @PutMapping("/{fleetId}/ship/{shipId}")
    public ResponseEntity<String> updateShip(
            @PathVariable long fleetId, @PathVariable long shipId,
            @ModelAttribute("ship") @Valid ShipDto ship,//
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            FleetDto fleet = fleetService.findById(fleetId);
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
            return ResponseEntity.badRequest().body("Invalid ship data!");
        }

        ShipDto newShip = shipService.findAll().stream()
                .filter(shipDto -> shipDto.getId().equals(ship.getId()))
                .findAny().orElseThrow();
        fleetService.updateShipForAFleet(fleetId, shipId, newShip);
        return ResponseEntity.ok().body("Ship switched with another.");
    }

    @DeleteMapping("/{fleetId}/ship/{shipId}")
    public ResponseEntity<Response> deleteById(@PathVariable long fleetId, @PathVariable long shipId) {
        fleetService.deleteShipFromFleet(fleetId, shipId);
        Response response = new Response();
        response.setMessage("Ship removed from fleet.");
        return ResponseEntity.ok().body(response);
    }
}
