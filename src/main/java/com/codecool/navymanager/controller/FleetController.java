package com.codecool.navymanager.controller;

import com.codecool.navymanager.dto.*;
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
@RequestMapping("/fleet")
public class FleetController {
    @Value( "${fleet.api.mapping}" )
    private String apiMapping;

    @Value( "${ship.api.mapping}" )
    private String shipApiMapping;

    @Value( "${rank.api.mapping}" )
    private String rankApiMapping;

    @Value( "${country.api.mapping}" )
    private String countryApiMapping;

    @Value( "${officer.api.mapping}" )
    private String officerApiMapping;

    private final RestTemplate restTemplate;

    public FleetController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<FleetDto> fleets = restTemplate.exchange(baseUrl + apiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<FleetDto>>() {}).getBody();
        model.addAttribute("fleets", fleets);
        return "fleet-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        FleetDto fleet = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, FleetDto.class).getBody();
        List<ShipDto> validShipValues =
                restTemplate.exchange(
                        baseUrl + shipApiMapping + "/available/country/" + fleet.getCountry().getId(),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<ShipDto>>() {}).getBody();
        model.addAttribute("fleet", fleet);
        model.addAttribute("validShipValues", validShipValues);
        return "fleet-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model, HttpServletRequest request){
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<CountryDto> validCountryValues = restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
        List<RankDto> validRankValues = restTemplate.exchange(baseUrl + rankApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RankDto>>() {}).getBody();
        List<OfficerDto> validCommanderValues =
                restTemplate.exchange(baseUrl + officerApiMapping + "/available", HttpMethod.GET, null,
                    new ParameterizedTypeReference<List<OfficerDto>>() {}).getBody();
        model.addAttribute("add", true);
        model.addAttribute("fleet", new FleetDto());
        model.addAttribute("validRankValues", validRankValues);
        model.addAttribute("validCommanderValues", validCommanderValues);
        model.addAttribute("validCountryValues", validCountryValues);
        return "fleet-form";
    }

    @RequestMapping(value="/add-with-form", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addFleetWithForm(
            HttpServletRequest request,
            @RequestBody FleetDto fleet,
            Model model) {
        HttpEntity<FleetDto> countryHttpEntity =
                Utils.createHttpEntityWithJSessionId(fleet, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.POST, countryHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable Long id, Model model, HttpServletRequest request) {
            String baseUrl = Utils.getBaseUrlFromRequest(request);
            FleetDto fleet = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, FleetDto.class).getBody();
            List<CountryDto> validCountryValues = restTemplate.exchange(baseUrl + countryApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<CountryDto>>() {}).getBody();
            List<RankDto> validRankValues = restTemplate.exchange(baseUrl + rankApiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RankDto>>() {}).getBody();
            List<OfficerDto> validCommanderValues =
                    restTemplate.exchange(baseUrl + officerApiMapping + "/available/fleet/" + id, HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<OfficerDto>>() {}).getBody();
            model.addAttribute("add", false);
            model.addAttribute("fleet", fleet);
            model.addAttribute("validRankValues", validRankValues);
            model.addAttribute("validCommanderValues", validCommanderValues);
            model.addAttribute("validCountryValues", validCountryValues);
            return "fleet-form";
    }

    @RequestMapping(value = "/{id}/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateFleetWithForm(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody FleetDto fleet,
            Model model) {
        HttpEntity<FleetDto> fleetHttpEntity =
                Utils.createHttpEntityWithJSessionId(fleet, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id, HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/ship/show-add-ship-form")
    public String showAddShipForm(
            @PathVariable Long id,
            Model model,
            HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        FleetDto fleet = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, FleetDto.class).getBody();
        List<ShipDto> validShipValues =
                restTemplate.exchange(
                        baseUrl + shipApiMapping + "/available/country/" + fleet.getCountry().getId(),
                            HttpMethod.GET, null,
                            new ParameterizedTypeReference<List<ShipDto>>() {}).getBody();
        model.addAttribute("add", true);
        model.addAttribute("fleet", fleet);
        model.addAttribute("chosenShip", new IdentityDto());
        model.addAttribute("validShipValues", validShipValues);
        return "fleet-ship-form";
    }

    @RequestMapping(value = "/{id}/ship/add-with-form" , method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> addShipToFleetWithForm(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody IdentityDto chosenShip) {
        HttpEntity<IdentityDto> chosenShipHttpEntity =
                Utils.createHttpEntityWithJSessionId(chosenShip, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id + "/ship", HttpMethod.POST, chosenShipHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{fleetId}/ship/{shipId}/show-update-ship-form")
    public String showUpdateShipForm(
            @PathVariable long fleetId,
            @PathVariable long shipId,
            Model model,
            HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        FleetDto fleet = restTemplate.getForEntity(baseUrl + apiMapping + "/" + fleetId, FleetDto.class).getBody();
        List<ShipDto> validShipValues =
                restTemplate.exchange(
                        baseUrl + shipApiMapping + "/available/country/" + fleet.getCountry().getId(),
                        HttpMethod.GET, null,
                        new ParameterizedTypeReference<List<ShipDto>>() {}).getBody();
        model.addAttribute("add", false);
        model.addAttribute("fleet", fleet);
        model.addAttribute("chosenShip", new IdentityDto(shipId));
        model.addAttribute("validShipValues", validShipValues);
        return "fleet-ship-form";
    }

    @RequestMapping(value = "/{id}/ship/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateShipInFleetWithForm(
            HttpServletRequest request,
            @PathVariable Long id,
            @RequestBody IdentityDto chosenShip) {
        HttpEntity<IdentityDto> chosenShipHttpEntity =
                Utils.createHttpEntityWithJSessionId(chosenShip, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id + "/ship/" + chosenShip.getId(), HttpMethod.PUT, chosenShipHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

