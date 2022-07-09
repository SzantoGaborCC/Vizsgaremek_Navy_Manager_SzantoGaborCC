package com.codecool.navymanager.controller;


import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.ShipClassDto;
import com.codecool.navymanager.dto.ShipDto;
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
@RequestMapping("/ship")
public class ShipController {
    @Value( "${ship.api.mapping}" )
    private String apiMapping;

    @Value( "${ship-class.api.mapping}" )
    private String shipClassApiMapping;

    @Value( "${country.api.mapping}" )
    private String countryApiMapping;

    @Value( "${officer.api.mapping}" )
    private String officerApiMapping;

    private final RestTemplate restTemplate;

    public ShipController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<ShipDto> ships =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<ShipDto>>() {}).getBody();
        model.addAttribute("ships", ships);
        return "ship-list";
    }


    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ShipDto ship = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, ShipDto.class).getBody();
        model.addAttribute("ship", ship);
        return "ship-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model, HttpServletRequest request){
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<OfficerDto> validCaptainValues =
                restTemplate.exchange(baseUrl + officerApiMapping + "/available", HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<OfficerDto>>() {}).getBody();
        List<ShipClassDto> validShipClassValues =
                restTemplate.exchange(baseUrl + shipClassApiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<ShipClassDto>>() {}).getBody();
        List<CountryDto> validCountryValues =
                restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
        model.addAttribute("add", true);
        model.addAttribute("ship", new ShipDto());
        model.addAttribute("validCaptainValues", validCaptainValues);
        model.addAttribute("validShipClassValues", validShipClassValues);
        model.addAttribute("validCountryValues", validCountryValues);
        return "ship-form";
    }

    @RequestMapping(value="/add-with-form", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addShipWithForm(
            HttpServletRequest request,
            @RequestBody ShipDto ship) {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntityWithJSessionId(ship, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.POST, shipHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ShipDto ship = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, ShipDto.class).getBody();
        List<OfficerDto> validCaptainValues =
                restTemplate.exchange(baseUrl + officerApiMapping + "/available/ship/" + id, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<OfficerDto>>() {}).getBody();
        List<ShipClassDto> validShipClassValues =
                restTemplate.exchange(baseUrl + shipClassApiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<ShipClassDto>>() {}).getBody();
        List<CountryDto> validCountryValues =
                restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
        model.addAttribute("add", false);
        model.addAttribute("ship", ship);
        model.addAttribute("validCaptainValues", validCaptainValues);
        model.addAttribute("validShipClassValues",validShipClassValues);
        model.addAttribute("validCountryValues", validCountryValues);
        return "ship-form";
    }

    @RequestMapping(value = "/{id}/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateShipWithForm(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody ShipDto ship) {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntityWithJSessionId(ship, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id, HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

