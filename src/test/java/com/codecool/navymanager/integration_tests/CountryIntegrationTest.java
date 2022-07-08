package com.codecool.navymanager.integration_tests;

import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.response.JsonResponse;
import com.codecool.navymanager.utilities.Utils;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class CountryIntegrationTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WebClient webClient = new WebClient();

    {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }
    private final String baseUrl = "/country";
    private final String baseUrlAPI = "/country/api";

    private final CountryDto[] data = {
            CountryDto.builder().name("Mexico").build(),
            CountryDto.builder().name("Chile").build(),
            CountryDto.builder().name("Argentina").build(),
            CountryDto.builder().name("Uruguay").build()
    };

    private final CountryDto countryToBeAddedFirst = CountryDto.builder().name("Uruguay").build();
    private final CountryDto countryToBeAddedFirstWithIdToTriggerError = CountryDto.builder().id(1L).name("Uruguay").build();
    private final CountryDto countryForUpdatedNoIdLeadsToError = CountryDto.builder().name("Brazil").build();
    private final CountryDto countryToBeUpdatedInvalidIdLeadsToError = CountryDto.builder().id(22L).name("Brazil").build();
    private final CountryDto countryWithUpdateDataValid = CountryDto.builder().id(4L).name("Brazil").build();
    private final CountryDto countryNullName = CountryDto.builder().id(4L).build();
    private final CountryDto countryEmptyName = CountryDto.builder().id(4L).name("").build();

    private final CountryDto countryWithUpdateData = CountryDto.builder().id(1L).name("Uruguay22").build();


    @Test
    @Order(1)
    void getAllCountriesShouldReturnOkAndEmptyArray() {
        ResponseEntity<CountryDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrlAPI, CountryDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(2)
    void postCountryShouldReturnOkDuplicateShouldReturnWithError() {
        HttpEntity<CountryDto> countryHttpEntity = Utils.createHttpEntity(countryToBeAddedFirst);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        countryHttpEntity = Utils.createHttpEntity(countryToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    void getTheAddedCountryShouldHaveIdOfOneAndNameUruguay() {
        ResponseEntity<CountryDto> responseEntity =
                testRestTemplate.getForEntity(baseUrlAPI + "/1", CountryDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getId());
        assertEquals("Uruguay", responseEntity.getBody().getName());
    }

    @Test
    @Order(4)
    void postMultipleCountriesThenCheckTheReturnedArrayUsingCountryNames() {
        HttpEntity<CountryDto> countryHttpEntity;
        for (CountryDto countryDto : data) {
            if (countryDto.getName().equals("Uruguay"))
                continue;
            countryHttpEntity = Utils.createHttpEntity(countryDto);
            testRestTemplate.postForEntity(baseUrlAPI, countryHttpEntity, JsonResponse.class);
        }
        ResponseEntity<CountryDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrlAPI, CountryDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustNames = Arrays.stream(data).map(CountryDto::getName).toList();
        List<String> returnedListJustNames =
                Arrays.stream(responseEntity.getBody()).map(CountryDto::getName).toList();
        assertTrue(dataAsListJustNames.containsAll(returnedListJustNames));
    }

    @Test
    @Order(5)
    void updateCountryWithNullIdShouldReturnIdError() {
        HttpEntity<CountryDto> countryHttpEntity = Utils.createHttpEntity(countryForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(6)
    void updateCountryWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<CountryDto> countryHttpEntity = Utils.createHttpEntity(countryWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateCountryIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<CountryDto> countryHttpEntity = Utils.createHttpEntity(countryToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/22", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void addCountryIfNameIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<CountryDto> countryHttpEntity = Utils.createHttpEntity(countryNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        countryHttpEntity = Utils.createHttpEntity(countryEmptyName);
        responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void updateCountryIfNameIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<CountryDto> countryHttpEntity = Utils.createHttpEntity(countryNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        countryHttpEntity = Utils.createHttpEntity(countryEmptyName);
        responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void deleteCountryIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeBadRequest() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void checkIfShowListPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-list-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "List of Countries");
    }

    @Test
    @Order(12)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Add New Country");
    }

    @Test
    @Order(13)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, countryToBeAddedFirst.getName() + " - Details");
    }

    @Test
    @Order(14)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Update Country");
    }

    @Test
    @Order(15)
    void updateCountryWithValidDataShouldReturnOk() {
        HttpEntity<CountryDto> countryHttpEntity =
                Utils.createHttpEntity(countryWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
