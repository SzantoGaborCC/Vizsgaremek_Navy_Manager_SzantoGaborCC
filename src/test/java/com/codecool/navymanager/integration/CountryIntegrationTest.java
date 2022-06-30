package com.codecool.navymanager.integration;

import com.codecool.navymanager.TestMessages;
import com.codecool.navymanager.TestUtilities;
import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.response.JsonResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
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

    private final CountryDto[] data = {
            CountryDto.builder().name("Mexico").build(),
            CountryDto.builder().name("Chile").build(),
            CountryDto.builder().name("Argentina").build(),
            CountryDto.builder().name("Uruguay").build()
    };

    private final CountryDto countryToBeAdded = CountryDto.builder().name("Uruguay").build();
    private final CountryDto countryForUpdatedNoIdLeadsToError = CountryDto.builder().name("Brazil").build();
    private final CountryDto countryToBeUpdatedInvalidIdLeadsToError = CountryDto.builder().id(22L).name("Brazil").build();
    private final CountryDto countryToBeUpdatedValid = CountryDto.builder().id(4L).name("Brazil").build();
    private final CountryDto countryNullName = CountryDto.builder().id(4L).build();
    private final CountryDto countryEmptyName = CountryDto.builder().id(4L).name("").build();

    @Test
    @Order(1)
    void getAllCountriesShouldReturnOkAndEmptyArray() {
        ResponseEntity<CountryDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, CountryDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(2)
    void postCountryShouldReturnOkWithAddedMessage() {
        HttpEntity<CountryDto> countryHttpEntity = TestUtilities.createHttpEntity(countryToBeAdded);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    void getTheAddedCountryShouldHaveIdOfOneAndNameUruguay() {
        ResponseEntity<CountryDto> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1", CountryDto.class);
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
            countryHttpEntity = TestUtilities.createHttpEntity(countryDto);
            testRestTemplate.postForEntity(baseUrl, countryHttpEntity, JsonResponse.class);
        }
        ResponseEntity<CountryDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, CountryDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustNames = Arrays.stream(data).map(CountryDto::getName).toList();
        List<String> returnedListJustNames =
                Arrays.stream(responseEntity.getBody()).map(CountryDto::getName).toList();
        assertTrue(dataAsListJustNames.containsAll(returnedListJustNames));
    }

    @Test
    @Order(5)
    void updateCountryWithNullIdShouldReturnIdError() {
        HttpEntity<CountryDto> countryHttpEntity = TestUtilities.createHttpEntity(countryForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(6)
    void updateCountryWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<CountryDto> countryHttpEntity = TestUtilities.createHttpEntity(countryToBeUpdatedValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateCountryIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<CountryDto> countryHttpEntity = TestUtilities.createHttpEntity(countryToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/22", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void addCountryIfNameIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<CountryDto> countryHttpEntity = TestUtilities.createHttpEntity(countryNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        countryHttpEntity = TestUtilities.createHttpEntity(countryEmptyName);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void updateCountryIfNameIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<CountryDto> countryHttpEntity = TestUtilities.createHttpEntity(countryNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        countryHttpEntity = TestUtilities.createHttpEntity(countryEmptyName);
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void deleteCountryIfExistsShouldReturnOkIfItDoesNotExistThenShouldBadRequest() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void checkIfShowListPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-list-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
    }

    @Test
    @Order(12)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
    }

    @Test
    @Order(13)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
    }

    @Test
    @Order(14)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
    }
}
