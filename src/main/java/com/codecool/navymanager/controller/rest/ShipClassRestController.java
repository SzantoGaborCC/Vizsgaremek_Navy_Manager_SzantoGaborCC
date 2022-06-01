package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.entityDTO.ShipClassDto;
import com.codecool.navymanager.service.ShipClassService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shipClass")
public class ShipClassRestController {
    private final ShipClassService shipClassService;

    public ShipClassRestController(ShipClassService shipClassService) {
        this.shipClassService = shipClassService;
    }

    @GetMapping
    public List<ShipClassDto> findAll() {
        return shipClassService.findAll();
    }

    @GetMapping("/{id}")
    public ShipClassDto findById(@PathVariable long id) {
        return shipClassService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody ShipClassDto shipClass) {
        shipClassService.add(shipClass);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody ShipClassDto shipClass, @PathVariable long id) {
        shipClassService.update(shipClass, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        shipClassService.deleteById(id);
    }
}
