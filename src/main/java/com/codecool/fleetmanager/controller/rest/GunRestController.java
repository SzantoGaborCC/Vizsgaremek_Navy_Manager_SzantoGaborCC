package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.model.Gun;
import com.codecool.fleetmanager.service.GunService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/gun")
public class GunRestController {
    private GunService gunService;

    public GunRestController(GunService gunService) {
        this.gunService = gunService;
    }

    @GetMapping
    public List<Gun> findAll() {
        System.out.println(gunService.findAll());
        return gunService.findAll();
    }

    @GetMapping("/{id}")
    public Gun findById(@PathVariable long id) {
        return gunService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody Gun gun) {
        gunService.add(gun);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Gun gun, @PathVariable long id) {
        gunService.update(gun, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        gunService.delete(id);
    }
}
