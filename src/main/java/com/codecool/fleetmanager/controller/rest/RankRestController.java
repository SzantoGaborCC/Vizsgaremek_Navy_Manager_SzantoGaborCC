package com.codecool.fleetmanager.controller.rest;

import com.codecool.fleetmanager.model.Rank;
import com.codecool.fleetmanager.service.RankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rank")
public class RankRestController {
    private RankService rankService;

    public RankRestController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    public List<Rank> findAll() {
        System.out.println(rankService.findAll());
        return rankService.findAll();
    }

    @GetMapping("/{id}")
    public Rank findById(@PathVariable long id) {
        return rankService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody Rank rank) {
        rankService.add(rank);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody Rank rank, @PathVariable long id) {
        rankService.update(rank, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        rankService.delete(id);
    }
}
