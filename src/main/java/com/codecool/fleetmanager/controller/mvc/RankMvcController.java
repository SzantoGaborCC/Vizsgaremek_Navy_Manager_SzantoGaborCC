package com.codecool.fleetmanager.controller.mvc;

import com.codecool.fleetmanager.DTO.RankDTO;
import com.codecool.fleetmanager.service.RankService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @GetMapping("/{id}")
    public RankDTO findById(@PathVariable Long id) {
        return rankService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("rank", new RankDTO());
        model.addAttribute("create", true);
        return "rank-form";
    }

    @PostMapping("/create")
    public String add(@Valid RankDTO rank, BindingResult result, Model model) {
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

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        rankService.delete(id);
        return "redirect:/rank-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, Model model) {
        try {
           RankDTO rank = rankService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("rank", rank);
            return "rank-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent rank!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @Valid RankDTO rank, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("create", false);
            return "rank-form";
        }
        rankService.update(rank, id);
        return "redirect:/rank-mvc";
    }
}

