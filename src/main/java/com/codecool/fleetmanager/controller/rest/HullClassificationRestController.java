package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.model.HullClassification;
import com.codecool.fleetmanager.service.HullClassificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/hullClassification")
public class HullClassificationRestController {
    private HullClassificationService hullClassificationService;

    public HullClassificationRestController(HullClassificationService hullClassificationService) {
        this.hullClassificationService = hullClassificationService;
    }

    @GetMapping
    public List<HullClassification> findAll() {
        System.out.println(hullClassificationService.findAll());
        return hullClassificationService.findAll();
    }

    @GetMapping("/{abbreviation}")
    public HullClassification findById(@PathVariable String abbreviation) {
        return hullClassificationService.findByAbbreviation(abbreviation);
    }

    @PostMapping
    public void add(@RequestBody HullClassification entity) {
        hullClassificationService.add(entity);
    }

    @PutMapping("/{abbreviation}")
    public void update(@RequestBody HullClassification entity, @PathVariable String abbreviation) {
        hullClassificationService.update(entity, abbreviation);
    }

    @DeleteMapping("/{abbreviation}")
    public void delete(@PathVariable String abbreviation) {
        hullClassificationService.delete(abbreviation);
    }
}
