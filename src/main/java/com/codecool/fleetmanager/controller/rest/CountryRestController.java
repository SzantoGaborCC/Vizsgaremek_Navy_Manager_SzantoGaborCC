package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.DTO.CountryDTO;
import com.codecool.fleetmanager.service.CountryService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/country")
public class CountryRestController {
    private final CountryService countryService;

    public CountryRestController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public List<CountryDTO> findAll() {
        return countryService.findAll();
    }

    @GetMapping("/{id}")
    public CountryDTO findById(@PathVariable long id) {
        return countryService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody CountryDTO country) {
        countryService.add(country);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody CountryDTO country, @PathVariable long id) {
        countryService.update(country, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        countryService.delete(id);
    }
}
