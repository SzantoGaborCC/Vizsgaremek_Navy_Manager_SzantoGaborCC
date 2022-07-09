package com.codecool.navymanager.controller;

import com.codecool.navymanager.dto.RankDto;
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
import java.util.Locale;

@Controller
@RequestMapping("/rank")
public class RankController {
    @Value( "${rank.api.mapping}" )
    private String apiMapping;

    private final RestTemplate restTemplate;

    public RankController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @GetMapping("/show-list-page")
    public String showListPage(Model model, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        List<RankDto> ranks = restTemplate.exchange(baseUrl + apiMapping, HttpMethod.GET, null,
                new ParameterizedTypeReference<List<RankDto>>() {}).getBody();
        model.addAttribute("ranks", ranks);
        return "rank-list";
    }

    @GetMapping("/{id}/show-details-page")
    public String showDetailsPage(@PathVariable Long id, Model model, Locale locale, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        RankDto rank = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, RankDto.class).getBody();
        model.addAttribute("rank", rank);
        return "rank-details";
    }

    @GetMapping("/show-add-form")
    public String showAddForm(Model model){
        model.addAttribute("add", true);
        model.addAttribute("rank", new RankDto());
        return "rank-form";
    }

    @RequestMapping(value="/add-with-form", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> addRankWithForm(
            HttpServletRequest request,
            @RequestBody RankDto rank) {
        HttpEntity<RankDto> rankHttpEntity =
                Utils.createHttpEntityWithJSessionId(rank, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping, HttpMethod.POST, rankHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }

    @GetMapping("/{id}/show-update-form")
    public String showUpdateForm(@PathVariable long id, Model model, Locale locale, HttpServletRequest request) {
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        RankDto rank = restTemplate.getForEntity(baseUrl + apiMapping + "/" + id, RankDto.class).getBody();
        model.addAttribute("add", false);
        model.addAttribute("rank", rank);
        return "rank-form";
    }

    @RequestMapping(value = "/{id}/update-with-form" , method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JsonResponse> updateRankWithForm(
            HttpServletRequest request,
            @PathVariable long id,
            @RequestBody RankDto rank,
            Model model) {
        HttpEntity<RankDto> rankHttpEntity =
                Utils.createHttpEntityWithJSessionId(rank, RequestContextHolder.currentRequestAttributes().getSessionId());
        String baseUrl = Utils.getBaseUrlFromRequest(request);
        ResponseEntity<JsonResponse> responseEntity =
                restTemplate.exchange(baseUrl + apiMapping + "/" + id, HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        if (responseEntity.getStatusCode() == HttpStatus.BAD_REQUEST) {
            return ResponseEntity.badRequest().body(responseEntity.getBody());
        }
        return ResponseEntity.ok().body(responseEntity.getBody());
    }
}

