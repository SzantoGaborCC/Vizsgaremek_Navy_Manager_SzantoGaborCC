package com.codecool.navymanager.controller;

import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.utilities.Utils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
@RequestMapping("/hull-classification")
public class HullClassificationController {
    @Value( "${hull-classification.api.mapping}" )
    private String apiMapping;
    private final RestTemplate restTemplate;

    public HullClassificationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<HullClassificationDto> hullClassifications =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<HullClassificationDto>>() {}).getBody();
        model.addAttribute("hullClassifications", hullClassifications);
        return "hull-classification-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        HullClassificationDto hullClassification =
                restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, HullClassificationDto.class).getBody();
        model.addAttribute("hullClassification", hullClassification);
        return "hull-classification-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("hullClassification", new HullClassificationDto());
        return "hull-classification-form";
    }

    @RequestMapping(value="/add-with-form", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> addHullClassificationWithForm(
            HttpServletRequest request,
            @RequestBody HullClassificationDto hullClassification) {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntityWithJSessionId(hullClassification,
                        RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.POST, hullClassificationHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        HullClassificationDto hullClassification =
            restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, HullClassificationDto.class).getBody();
        model.addAttribute("add", false);
        model.addAttribute("hullClassification", hullClassification);
        return "hull-classification-form";
    }

    @RequestMapping(value = "/{id}/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateHullClassificationWithForm(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody HullClassificationDto hullClassification) {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntityWithJSessionId(hullClassification,
                        RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id, HttpMethod.PUT,
                        hullClassificationHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

