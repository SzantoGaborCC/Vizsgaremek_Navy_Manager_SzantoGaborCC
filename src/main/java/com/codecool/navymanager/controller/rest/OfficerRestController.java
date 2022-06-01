package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.entityDTO.OfficerDto;
import com.codecool.navymanager.service.OfficerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officer")
public class OfficerRestController {
    private final OfficerService officerService;

    public OfficerRestController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @GetMapping
    public List<OfficerDto> findAll() {
        return officerService.findAll();
    }

    @GetMapping("/{id}")
    public OfficerDto findById(@PathVariable long id) {
        return officerService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody OfficerDto officer) {
        officerService.save(officer);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody OfficerDto officer, @PathVariable long id) {
        officerService.update(officer, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        officerService.deleteById(id);
    }
}
