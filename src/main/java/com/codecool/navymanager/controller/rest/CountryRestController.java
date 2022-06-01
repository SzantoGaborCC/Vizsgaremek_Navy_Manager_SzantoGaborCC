package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.entityDTO.CountryDto;
import com.codecool.navymanager.service.CountryService;
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
    public List<CountryDto> findAll() {
        return countryService.findAll();
    }

    @GetMapping("/{id}")
    public CountryDto findById(@PathVariable long id) {
        return countryService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody CountryDto country) {
        countryService.save(country);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody CountryDto country, @PathVariable long id) {
        countryService.save(country);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        countryService.deleteById(id);
    }
}
