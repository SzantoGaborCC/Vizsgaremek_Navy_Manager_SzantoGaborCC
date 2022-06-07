package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.entityDTO.RankDto;
import com.codecool.navymanager.service.RankService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@Controller
@RequestMapping("/rank-mvc")
public class RankMvcController {
    private final RankService rankService;

    public RankMvcController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    public String listRanks(Model model) {
        model.addAttribute("ranks", rankService.findAll());
        return "rank-list";
    }

    @GetMapping("/{precedence}")
    public RankDto findById(@PathVariable int precedence) {
        return rankService.findByPrecedence(precedence);
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("create", true);
        model.addAttribute("rank", new RankDto());
        return "rank-form";
    }

    @PostMapping("/create")
    public String add(@ModelAttribute("rank") @Valid RankDto rank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", true);
            return "rank-form";
        }
        try {
           rankService.add(rank);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid rank data!", e);
        }
        return "redirect:/rank-mvc";
    }

    @GetMapping("/delete/{precedence}")
    public String deleteById(@PathVariable int precedence) {
        rankService.deleteByPrecedence(precedence);
        return "redirect:/rank-mvc";
    }

    @GetMapping("/update/{precedence}")
    public String showUpdateForm(@PathVariable int precedence, Model model) {
        try {
            RankDto rank = rankService.findByPrecedence(precedence);
            model.addAttribute("create", false);
            model.addAttribute("rank", rank);
            return "rank-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent rank!", e);
        }
    }
    //todo: should have serial type of id, we cant update precedence this way, foreign key violation
    @PostMapping("/update/{precedence}")
    public String update(@PathVariable int precedence, @ModelAttribute("rank") @Valid RankDto rank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "rank-form";
        }
        rankService.update(rank, precedence);
        return "redirect:/rank-mvc";
    }
}

