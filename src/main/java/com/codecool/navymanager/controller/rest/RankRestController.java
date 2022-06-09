package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.entityDTO.RankDto;
import com.codecool.navymanager.service.RankService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@RestController
//@RequestMapping("/rank")
public class RankRestController {
    private final RankService rankService;

    public RankRestController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    public List<RankDto> findAll() {
        return rankService.findAll();
    }

    @GetMapping("/{id}")
    public RankDto findByPrecedence(@PathVariable long id) {
        return rankService.findById(id);
    }

    @PostMapping
    public void add(@RequestBody RankDto rank) {
        rankService.add(rank);
    }

    @PutMapping("/{id}")
    public void update(@RequestBody RankDto rank, @PathVariable long id) {
        rankService.update(rank, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable long id) {
        rankService.deleteById(id);
    }
}
