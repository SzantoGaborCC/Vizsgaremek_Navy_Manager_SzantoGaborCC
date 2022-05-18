package com.codecool.navymanager.controller.mvc;


import com.codecool.navymanager.DTO.CountryDTO;
import com.codecool.navymanager.DTO.OfficerDTO;
import com.codecool.navymanager.DTO.RankDTO;
import com.codecool.navymanager.service.OfficerService;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping("/officer-mvc")
public class OfficerMvcController {
    private final OfficerService officerService;

    public OfficerMvcController(OfficerService officerService) {
        this.officerService = officerService;
    }

    @GetMapping
    public String listOfficers(Model model) {
        model.addAttribute("officers", officerService.findAll());
        return "officer-list";
    }

    @GetMapping("/{id}")
    public OfficerDTO findById(@PathVariable Long id) {
        return officerService.findById(id);
    }

    @GetMapping("/create")
    public String showCreateForm(Model model){
        model.addAttribute("officer", new OfficerDTO());
        model.addAttribute("create", true);
        model.addAttribute("validRankValues", officerService.getValidRankValues());
        model.addAttribute("validCountryValues", officerService.getValidCountryValues());
        return "officer-form";
    }

    @PostMapping("/create")
    public String add(@Valid OfficerDTO officer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            model.addAttribute("create", true);
            model.addAttribute("officer", officer);
            model.addAttribute("validRankValues", officerService.getValidRankValues());
            model.addAttribute("validCountryValues", officerService.getValidCountryValues());
            return "officer-form";
        }
        try {
           officerService.add(officer);
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid officer data!", e);
        }
        return "redirect:/officer-mvc";
    }

    @GetMapping("/delete/{id}")
    public String deleteById(@PathVariable Long id) {
        officerService.delete(id);
        return "redirect:/officer-mvc";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable Long id, OfficerDTO officerDTO ,Model model) {
        try {
            OfficerDTO officer = officerService.findById(id);
            model.addAttribute("create", false);
            model.addAttribute("officer", officer);
            model.addAttribute("validRankValues", officerService.getValidRankValues());
            model.addAttribute("validCountryValues", officerService.getValidCountryValues());
            return "officer-form";
        } catch (Exception e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Nonexistent ship class!", e);
        }
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable long id, @ModelAttribute("officer") @Valid OfficerDTO officer, BindingResult result, Model model) {
        if (result.hasErrors()) {
            //System.out.println(result.getAllErrors());
            model.addAttribute("create", false);
            System.out.println("is there an officer: " + model.getAttribute("officer"));
            model.addAttribute("officer", officer);
            model.addAttribute("validRankValues", officerService.getValidRankValues());
            model.addAttribute("validCountryValues", officerService.getValidCountryValues());
            return "officer-form";
        }
        officerService.update(officer, id);
        return "redirect:/officer-mvc";
    }

  /* @InitBinder
    public void registerDateFormatter(WebDataBinder binder) {
        binder.registerCustomEditor(
                Date.class,
                new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), false));
    }*/

    /*@InitBinder
    public void registerRankDTOConverter(WebDataBinder binder) {
        binder.registerCustomEditor(
                RankDTO.class,
                new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        setValue(officerService.getValidRankValues().stream()
                                .filter(rankDTO -> rankDTO.getDesignation().equals(text))
                                .findAny().orElseThrow()
                        );
                    }
                });
    }

    @InitBinder
    public void registerCountryDTOConverter(WebDataBinder binder) {
        binder.registerCustomEditor(
                CountryDTO.class,
                new PropertyEditorSupport() {
                    @Override
                    public void setAsText(String text) throws IllegalArgumentException {
                        setValue(officerService.getValidCountryValues().stream()
                                .filter(countryDTO -> countryDTO.getName().equals(text))
                                .findAny().orElseThrow()
                        );
                    }
                });
    }*/
}

