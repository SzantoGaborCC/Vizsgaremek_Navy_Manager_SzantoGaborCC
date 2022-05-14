package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.model.Country;
import com.codecool.fleetmanager.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryRestController {
    private CountryService countryService;

    public CountryRestController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<Country> findAll() {
        return countryService.findAll();
    }

    @GetMapping("/{id}")
    public Country findById(@PathVariable long id) {
        return countryService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody Country country) {
        countryService.add(country);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Country country, @PathVariable long id) {
        countryService.update(country, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        countryService.delete(id);
    }
}
