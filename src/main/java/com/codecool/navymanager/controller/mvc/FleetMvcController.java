package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.entityDTO.FleetDto;
import com.codecool.navymanager.entityDTO.ShipDto;
import com.codecool.navymanager.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;


@Controller
@RequestMapping("/fleet-mvc")
public class FleetMvcController {
    private final FleetService fleetService;
    private final OfficerService officerService;
    private final RankService rankService;
    private final CountryService countryService;

    private final ShipService shipService;

    public FleetMvcController(FleetService fleetService,
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

    @GetMapping("/details/{id}")
    public String showDetails(@PathVariable Long id, Model model) {
        FleetDto fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-details";
    }

    @GetMapping("/add")
    public String showCreateForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("fleet", new FleetDto());
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCommanderValues", null);
        model.addAttribute("validCountryValues", countryService.findAll());
        return "fleet-form";
    }

    @PostMapping("/add")
    public ResponseEntity<String> add(@ModelAttribute("fleet") @Valid FleetDto fleet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", null);
            model.addAttribute("validCountryValues", countryService.findAll());
            return ResponseEntity.badRequest().body("Invalid fleet data!");
        }

           fleetService.add(fleet);

        return ResponseEntity.ok().body("Fleet added.");
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        fleetService.deleteById(id);
        return "redirect:/fleet-mvc";
    }

    @GetMapping("/update/{id}")
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
    @PutMapping("/update/{id}")
    public ResponseEntity<String> update(@PathVariable long id, @ModelAttribute("fleet") @Valid FleetDto fleet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAvailableOfficersForFleet(fleet));
            model.addAttribute("validCountryValues", countryService.findAll());
            return ResponseEntity.badRequest().body("Invalid fleet data!");
        }
        fleetService.update(fleet, id);
        return ResponseEntity.ok().body("Fleet updated.");
    }

    @GetMapping("/add-ship/{id}")
    public String showAddShipForm(
            @PathVariable Long id,
            Model model) {
        model.addAttribute("add", true);
        FleetDto fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        model.addAttribute("ship", new ShipDto());//
        //model.addAttribute("shipId", new IdentityDTO());
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @PostMapping("/add-ship/{id}")
    public ResponseEntity<String> addShip(
            @PathVariable Long id,
            //@ModelAttribute("shipId") @Valid IdentityDTO shipId,
            @ModelAttribute("ship") @Valid ShipDto ship,//
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            FleetDto fleet = fleetService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
            return ResponseEntity.badRequest().body("Invalid ship data!");
        }
        //fleetService.addShipToFleet(id, shipId.getValue());
        fleetService.addShipToFleet(id, ship);
        return ResponseEntity.ok().body("Ship added to fleet.");
    }

    @GetMapping("/update-ship/{fleetId}/ship/{shipId}")
    public String showUpdateShipForm(
            @PathVariable long fleetId, @PathVariable long shipId,
            Model model) {

        FleetDto fleet = fleetService.findById(fleetId);
        ShipDto ship = shipService.findById(shipId);
        model.addAttribute("add", false);
        model.addAttribute("fleet", fleet);
        //model.addAttribute("shipId", new IdentityDTO(ship.getId()));
        model.addAttribute("ship", ship);//
        model.addAttribute("validShipValues", shipService.findAvailableShipsByCountry(fleet.getCountry()));
        return "fleet-ship-form";
    }

    @PutMapping("/update-ship/{fleetId}/ship/{oldShipId}")
    public ResponseEntity<String> updateShip(
            @PathVariable long fleetId, @PathVariable long oldShipId,
            //@ModelAttribute("shipId") @Valid IdentityDTO shipId,
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
        fleetService.updateShipForAFleet(fleetId, oldShipId, newShip);
        return ResponseEntity.ok().body("Ship switched with another.");
    }

    @DeleteMapping("/delete-ship/{fleetId}/ship/{shipId}")
    public ResponseEntity<String> deleteById(@PathVariable long fleetId, @PathVariable long shipId) {
        fleetService.deleteShipFromFleet(fleetId, shipId);
        return ResponseEntity.ok().body("Ship removed from fleet.");
    }

    /*@InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(
                ShipDTO.class,
                new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        System.out.println("--------------------------SHIP CONVERTER PROPERTY EDITOR CALLED!!!!!!!!!!------------------------");
                        System.out.println("ship name given to converter: " + text);
                        var ship = shipService.findAll().stream()
                                .filter(shipDTO -> shipDTO.getName().equals(text))
                                .findAny().orElseThrow();
                        System.out.println("Ship id found by shipService: " + ship.getId());
                        setValue(ship);
                    }
                }
        );
    }*/
}

