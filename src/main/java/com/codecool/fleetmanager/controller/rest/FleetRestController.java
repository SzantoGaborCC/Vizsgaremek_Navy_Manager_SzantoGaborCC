package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.model.Fleet;
import com.codecool.fleetmanager.service.FleetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/fleet")
public class FleetRestController {
    private FleetService fleetService;

    public FleetRestController(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @GetMapping
    public List<Fleet> findAll() {
        return fleetService.findAll();
    }

    @GetMapping("/{id}")
    public Fleet findById(@PathVariable long id) {
        return fleetService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody Fleet fleet) {
        fleetService.add(fleet);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Fleet fleet, @PathVariable long id) {
        fleetService.update(fleet, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        fleetService.delete(id);
    }
}
