package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.DTO.HullClassificationDTO;
import com.codecool.fleetmanager.service.HullClassificationService;
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
    public List<HullClassificationDTO> findAll() {
        return hullClassificationService.findAll();
    }

    @GetMapping("/{abbr}")
    public HullClassificationDTO findById(@PathVariable String abbr) {
        return hullClassificationService.findByAbbreviation(abbr);
    }

    @PostMapping
    public void add(@RequestBody HullClassificationDTO hullClassification) {
        hullClassificationService.add(hullClassification);
    }

    @PutMapping("/{abbr}")
    public void update(@RequestBody HullClassificationDTO hullClassification, @PathVariable String abbr) {
        hullClassificationService.update(hullClassification, abbr);
    }

    @DeleteMapping("/{abbr}")
    public void delete(@PathVariable String abbr) {
        hullClassificationService.delete(abbr);
    }
}
