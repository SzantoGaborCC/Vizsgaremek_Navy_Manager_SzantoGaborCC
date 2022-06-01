package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.entityDTO.HullClassificationDto;
import com.codecool.navymanager.service.HullClassificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hullClassification")
public class HullClassificationRestController {
    private final HullClassificationService hullClassificationService;

    public HullClassificationRestController(HullClassificationService hullClassificationService) {
        this.hullClassificationService = hullClassificationService;
    }

    @GetMapping
    public List<HullClassificationDto> findAll() {
        return hullClassificationService.findAll();
    }

    @GetMapping("/{abbr}")
    public HullClassificationDto findById(@PathVariable String abbr) {
        return hullClassificationService.findByAbbreviation(abbr);
    }

    @PostMapping
    public void add(@RequestBody HullClassificationDto hullClassification) {
        hullClassificationService.save(hullClassification);
    }

    @PutMapping("/{abbr}")
    public void update(@RequestBody HullClassificationDto hullClassification, @PathVariable String abbr) {
        hullClassificationService.save(hullClassification);
    }

    @DeleteMapping("/{abbr}")
    public void delete(@PathVariable String abbr) {
        hullClassificationService.deleteByAbbreviation(abbr);
    }
}
