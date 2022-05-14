package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.model.Ship;
import com.codecool.fleetmanager.service.ShipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ship")
public class ShipRestController {
    private ShipService shipService;

    public ShipRestController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    public List<Ship> findAll() {
        return shipService.findAll();
    }

    @GetMapping("/{id}")
    public Ship findById(@PathVariable long id) {
        return shipService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody Ship ship) {
        shipService.add(ship);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Ship ship, @PathVariable long id) {
        shipService.update(ship, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        shipService.delete(id);
    }
}
