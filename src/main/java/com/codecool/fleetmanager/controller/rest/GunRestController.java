package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.DTO.GunDTO;
import com.codecool.fleetmanager.service.GunService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gun")
public class GunRestController {
    private final GunService gunService;

    public GunRestController(GunService gunService) {
        this.gunService = gunService;
    }

    @GetMapping
    public List<GunDTO> findAll() {
        return gunService.findAll();
    }

    @GetMapping("/{id}")
    public GunDTO findById(@PathVariable long id) {
        return gunService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody GunDTO gun) {
        gunService.add(gun);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody GunDTO gun, @PathVariable long id) {
        gunService.update(gun, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        gunService.delete(id);
    }
}
