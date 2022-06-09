package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.entityDTO.FleetDto;
import com.codecool.navymanager.service.FleetService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/fleet")
public class FleetRestController {
    private final FleetService fleetService;

    public FleetRestController(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @GetMapping
    public List<FleetDto> findAll() {
        return fleetService.findAll();
    }

    @GetMapping("/{id}")
    public FleetDto findById(@PathVariable long id) {
        return fleetService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody FleetDto fleet) {
        fleetService.add(fleet);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody FleetDto fleet, @PathVariable long id) {
        fleetService.update(fleet, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        fleetService.deleteById(id);
    }
}
