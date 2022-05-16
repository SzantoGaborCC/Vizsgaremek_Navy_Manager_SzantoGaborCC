package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.DTO.ShipDTO;
import com.codecool.fleetmanager.service.ShipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ship")
public class ShipRestController {
    private final ShipService shipService;

    public ShipRestController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    public List<ShipDTO> findAll() {
        return shipService.findAll();
    }

    @GetMapping("/{id}")
    public ShipDTO findById(@PathVariable long id) {
        return shipService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody ShipDTO ship) {
        shipService.add(ship);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody ShipDTO ship, @PathVariable long id) {
        shipService.update(ship, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        shipService.delete(id);
    }
}
