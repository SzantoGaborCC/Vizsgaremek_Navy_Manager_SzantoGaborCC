package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.model.Officer;
import com.codecool.fleetmanager.service.OfficerService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officer")
public class OfficerRestController {
    private OfficerService officerService;

    public OfficerRestController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @GetMapping
    public List<Officer> findAll() {
        return officerService.findAll();
    }

    @GetMapping("/{id}")
    public Officer findById(@PathVariable long id) {
        return officerService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody Officer officer) {
        officerService.add(officer);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Officer officer, @PathVariable long id) {
        officerService.update(officer, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        officerService.delete(id);
    }
}
