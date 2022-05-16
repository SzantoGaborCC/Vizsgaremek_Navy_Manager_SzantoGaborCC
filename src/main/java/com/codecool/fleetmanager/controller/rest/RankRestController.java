package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.DTO.RankDTO;
import com.codecool.fleetmanager.service.RankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rank")
public class RankRestController {
    private final RankService rankService;

    public RankRestController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    public List<RankDTO> findAll() {
        return rankService.findAll();
    }

    @GetMapping("/{id}")
    public RankDTO findById(@PathVariable long id) {
        return rankService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody RankDTO rank) {
        rankService.add(rank);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody RankDTO rank, @PathVariable long id) {
        rankService.update(rank, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        rankService.delete(id);
    }
}
