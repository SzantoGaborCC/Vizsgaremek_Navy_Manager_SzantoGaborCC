package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.dto.ShipDto;
import com.codecool.navymanager.response.Response;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.OfficerService;
import com.codecool.navymanager.service.ShipClassService;
import com.codecool.navymanager.service.ShipService;
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
@RequestMapping("/ship")
public class ShipController {
    private final ShipService shipService;
    private final ShipClassService shipClassService;
    private final OfficerService officerService;
    private final CountryService countryService;

    public ShipController(ShipService shipService, ShipClassService shipClassService,
                          OfficerService officerService, CountryService countryService) {
        this.shipService = shipService;
        this.shipClassService = shipClassService;
        this.officerService = officerService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listShips(Model model) {
        model.addAttribute("ships", shipService.findAll());
        return "ship-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        ShipDto ship = shipService.findById(id);
        model.addAttribute("ship", ship);
        return "ship-details";
    }

    @GetMapping("/show-add-form")
    public String showCreateForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("ship", new ShipDto());
        model.addAttribute("validCaptainValues", null);
        model.addAttribute("validShipClassValues", shipClassService.findAll());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "ship-form";
    }

    @PostMapping
    public ResponseEntity<Response> add(@ModelAttribute("ship") @Valid ShipDto ship, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validCaptainValues", null);
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid country data!");
            return ResponseEntity.badRequest().body(response);
        }
        shipService.add(ship);
        response.setMessage("Ship was added.");
        return ResponseEntity.ok().body(response);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        shipService.deleteById(id);
        return ResponseEntity.ok().body("Ship was removed.");
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            ShipDto ship = shipService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("ship", ship);
            model.addAttribute("validCaptainValues", officerService.findAvailableOfficersForShip(ship));
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "ship-form";
        } catch (Exception e) {
            e.printStackTrace();
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship!", e);

        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable long id, @ModelAttribute("ship") @Valid ShipDto ship, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validCaptainValues", officerService.findAvailableOfficersForShip(ship));
            model.addAttribute("validShipClassValues", shipClassService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid ship data!");
            return ResponseEntity.badRequest().body(response);
        }
        shipService.update(ship, id);
        response.setMessage("Ship was updated.");
        return ResponseEntity.ok().body(response);
    }
}

