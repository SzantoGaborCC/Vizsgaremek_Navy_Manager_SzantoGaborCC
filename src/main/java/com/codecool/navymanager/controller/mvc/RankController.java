package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.dto.RankDto;
import com.codecool.navymanager.response.Response;
import com.codecool.navymanager.service.RankService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/rank")
public class RankController {
    private final RankService rankService;

    public RankController(RankService rankService) {
        this.rankService = rankService;
    }

    @GetMapping
    public String listRanks(Model model) {
        model.addAttribute("ranks", rankService.findAll());
        return "rank-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        RankDto rank = rankService.findById(id);
        model.addAttribute("rank", rank);
        return "rank-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("rank", new RankDto());
        return "rank-form";
    }

    @PostMapping
    public ResponseEntity<Response> add(@ModelAttribute("rank") @Valid RankDto rank, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid rank data!");
            return ResponseEntity.badRequest().body(response);
        }
        rankService.add(rank);
        response.setMessage("Rank was added.");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable long id) {
        rankService.deleteById(id);
        return ResponseEntity.ok().body("Rank was removed.");
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable long id, Model model) {
        RankDto rank = rankService.findById(id);
        model.addAttribute("add", false);
        model.addAttribute("rank", rank);
        return "rank-form";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable long id, @ModelAttribute("rank") @Valid RankDto rank, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid rank data!");
            return ResponseEntity.badRequest().body(response);
        }
        rankService.update(rank, id);
        response.setMessage("Rank was updated.");
        return ResponseEntity.ok().body(response);
    }
}

