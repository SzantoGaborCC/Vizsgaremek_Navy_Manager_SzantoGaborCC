package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.DTO.*;
import com.codecool.navymanager.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;

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
        FleetDTO fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        return "fleet-details";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("create", true);
        model.addAttribute("fleet", new FleetDTO());
        model.addAttribute("validRankValues", rankService.findAll());
        model.addAttribute("validCommanderValues", officerService.findAll());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "fleet-form";
    }

    @PostMapping("/create")
    public String add(@ModelAttribute("fleet") @Valid FleetDTO fleet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
        }
        try {
           fleetService.add(fleet);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid fleet data!", e);
        }
        return "redirect:/fleet-mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        fleetService.delete(id);
        return "redirect:/fleet-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
            FleetDTO fleet = fleetService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent fleet!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("fleet") @Valid FleetDTO fleet, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            model.addAttribute("validRankValues", rankService.findAll());
            model.addAttribute("validCommanderValues", officerService.findAll());
            model.addAttribute("validCountryValues", countryService.findAll());
            return "fleet-form";
        }
        fleetService.update(fleet, id);
        return "redirect:/fleet-mvc";
    }

    @GetMapping("/add-ship/{id}")
    public String showAddShipForm(
            @PathVariable Long id,
            Model model) {
        model.addAttribute("add", true);
        FleetDTO fleet = fleetService.findById(id);
        model.addAttribute("fleet", fleet);
        model.addAttribute("ship", new ShipDTO());
        System.out.println("ship added to model: " + model.getAttribute("ship"));
        model.addAttribute("validShipValues", shipService.findByCountryId(fleet.getCountry().getId()));
        return "fleet-ship-form";
    }

    @PostMapping("/add-ship/{id}")
    public String addShip(
            @PathVariable Long id,
            @ModelAttribute("ship") @Valid ShipDTO ship,
            Model model, BindingResult result) {
        System.out.println("the ship   at post: " + ship);
        if (result.hasErrors()) {
            System.out.println("Errors!!!!");
            FleetDTO fleet = fleetService.findById(id);
            model.addAttribute("add", true);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findByCountryId(fleet.getCountry().getId()));
            return "fleet-ship-form";
        }
        System.out.println("ship id: " + ship.getId());
        fleetService.addShipToFleet(id, ship.getId());
        return "redirect:/fleet-mvc/details/" + id;
    }

    //todo: check !addition!, update of ship from fleets
    @GetMapping("/update-ship/{fleetId}/ship/{shipId}")
    public String showUpdateShipForm(
            @PathVariable long fleetId, @PathVariable long shipId,
            Model model) {

        FleetDTO fleet = fleetService.findById(fleetId);
        ShipDTO ship = shipService.findById(shipId);
        model.addAttribute("add", false);
        model.addAttribute("fleet", fleet);
        model.addAttribute("ship", ship);
        model.addAttribute("validShipValues", shipService.findByCountryId(fleet.getCountry().getId()));
        return "fleet-ship-form";
    }

    @PostMapping("/update-ship/{fleetId}/ship/{shipId}")
    public String updateShip(
            @PathVariable long fleetId, @PathVariable long shipId,
            @ModelAttribute("ship") @Valid ShipDTO ship,
            Model model, BindingResult result) {
        if (result.hasErrors()) {
            FleetDTO fleet = fleetService.findById(fleetId);
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validShipValues", shipService.findByCountryId(fleet.getCountry().getId()));
            return "fleet-ship-form";
        }
        fleetService.updateShipForAFleet(fleetId, shipId, ship.getId());
        return "redirect:/fleet-mvc/details/" + fleetId;
    }

    @GetMapping("/delete-ship/{fleetId}/ship/{shipId}")
    public String deleteById(@PathVariable long fleetId, @PathVariable long shipId) {
        fleetService.deleteShipFromFleet(fleetId, shipId);
        return "redirect:/fleet-mvc/details/" + fleetId;
    }

    @InitBinder
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
    }
}

