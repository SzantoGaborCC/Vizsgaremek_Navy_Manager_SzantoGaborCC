package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.entityDTO.ShipDto;
import com.codecool.navymanager.service.ShipService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/ship")
public class ShipRestController {
    private final ShipService shipService;

    public ShipRestController(ShipService shipService) {
        this.shipService = shipService;
    }

    @GetMapping
    public List<ShipDto> findAll() {
        return shipService.findAll();
    }

    @GetMapping("/{id}")
    public ShipDto findById(@PathVariable long id) {
        return shipService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody ShipDto ship) {
        shipService.add(ship);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody ShipDto ship, @PathVariable long id) {
        shipService.update(ship, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        shipService.deleteById(id);
    }
}
