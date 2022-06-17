package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.service.HullClassificationService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/hullClassification")
public class HullClassificationRestController {
    private final HullClassificationService hullClassificationService;

    public HullClassificationRestController(HullClassificationService hullClassificationService) {
        this.hullClassificationService = hullClassificationService;
    }

    @GetMapping
    public List<HullClassificationDto> findAll() {
        return hullClassificationService.findAll();
    }

    @GetMapping("/{id}")
    public HullClassificationDto findById(@PathVariable long id) {
        return hullClassificationService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody HullClassificationDto hullClassification) {
        hullClassificationService.add(hullClassification);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody HullClassificationDto hullClassification, @PathVariable long id) {
        hullClassificationService.update(hullClassification, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        hullClassificationService.deleteById(id);
    }
}
