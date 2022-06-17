package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.service.GunService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/gun")
public class GunRestController {
    private final GunService gunService;

    public GunRestController(GunService gunService) {
        this.gunService = gunService;
    }

    @GetMapping
    public List<GunDto> findAll() {
        return gunService.findAll();
    }

    @GetMapping("/{id}")
    public GunDto findById(@PathVariable long id) {
        return gunService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody GunDto gun) {
        gunService.add(gun);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody GunDto gun, @PathVariable long id) {
        gunService.update(gun, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        gunService.deleteById(id);
    }
}
