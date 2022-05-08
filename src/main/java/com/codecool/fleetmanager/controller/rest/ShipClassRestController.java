package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.model.ShipClass;
import com.codecool.fleetmanager.service.ShipClassService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipClass")
public class ShipClassRestController {
    private ShipClassService shipClassService;

    public ShipClassRestController(ShipClassService shipClassService) {
        this.shipClassService = shipClassService;
    }

    @GetMapping
    public List<ShipClass> findAll() {
        return shipClassService.findAll();
    }

    @GetMapping("/{id}")
    public ShipClass findById(@PathVariable long id) {
        return shipClassService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody ShipClass entity) {
        shipClassService.add(entity);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody ShipClass entity, @PathVariable long id) {
        shipClassService.update(entity, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        shipClassService.delete(id);
    }
}
