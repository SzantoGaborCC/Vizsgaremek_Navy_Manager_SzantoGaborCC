package com.codecool.navymanager.controller.mvc;

import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.response.Response;
import com.codecool.navymanager.service.CountryService;
import com.codecool.navymanager.service.GunService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/gun")
public class GunController {
    private final GunService gunService;
    private final CountryService countryService;

    public GunController(GunService gunService, CountryService countryService) {
        this.gunService = gunService;
        this.countryService = countryService;
    }

    @GetMapping
    public String listGuns(Model model) {
        model.addAttribute("guns", gunService.findAll());
        return "gun-list";
    }

    @GetMapping("/{id}")
    public String getDetails(@PathVariable Long id, Model model) {
        GunDto gun = gunService.findById(id);
        model.addAttribute("gun", gun);
        return "gun-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("gun", new GunDto());
        model.addAttribute("validCountryValues", countryService.findAll());
        return "gun-form";
    }

    @PostMapping
    public ResponseEntity<Response> add(@ModelAttribute("gun") @Valid GunDto gun, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", true);
            model.addAttribute("validCountryValues", countryService.findAll());
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid gun data!");
            return ResponseEntity.badRequest().body(response);
        }
        gunService.add(gun);
        response.setMessage("Gun was added.");
        return ResponseEntity.ok().body(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteById(@PathVariable Long id) {
        gunService.deleteById(id);
        return ResponseEntity.ok().body("Gun was removed.");
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable long id, Model model) {
            GunDto gun = gunService.findById(id);
            model.addAttribute("add", false);
            model.addAttribute("gun", gun);
            model.addAttribute("validCountryValues", countryService.findAll());
            return "gun-form";
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> update(@PathVariable long id, @ModelAttribute("gun") @Valid GunDto gun, BindingResult result, Model model) {
        Response response = new Response();
        if (result.hasErrors()) {
            model.addAttribute("add", false);
            model.addAttribute("validCountryValues", countryService.findAll());
            response.setErrorMessages(result.getFieldErrors().stream()
                    .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage)));
            response.setMessage("Invalid gun data!");
            return ResponseEntity.badRequest().body(response);
        }
        gunService.update(gun, id);
        response.setMessage("Gun was updated.");
        return ResponseEntity.ok().body(response);
    }
}

