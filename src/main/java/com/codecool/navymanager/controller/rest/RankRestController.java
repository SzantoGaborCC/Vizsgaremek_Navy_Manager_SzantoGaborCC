package com.codecool.navymanager.controller.rest;

import com.codecool.navymanager.entityDTO.RankDto;
import com.codecool.navymanager.service.RankService;
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
    public List<RankDto> findAll() {
        return rankService.findAll();
    }

    @GetMapping("/{precedence}")
    public RankDto findByPrecedence(@PathVariable int precedence) {
        return rankService.findByPrecedence(precedence);
    }

    @PostMapping
    public void add(@RequestBody RankDto rank) {
        rankService.add(rank);
    }

    @PutMapping("/{precedence}")
    public void update(@RequestBody RankDto rank, @PathVariable int precedence) {
        rankService.update(rank, precedence);
    }

    @DeleteMapping("/{precedence}")
    public void delete(@PathVariable int precedence) {
        rankService.deleteByPrecedence(precedence);
    }
}
