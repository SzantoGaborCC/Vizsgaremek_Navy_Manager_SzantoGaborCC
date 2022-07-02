package com.codecool.navymanager.integration;

import com.codecool.navymanager.TestUtilities;
import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.GunDto;
import com.codecool.navymanager.dto.HullClassificationDto;
import com.codecool.navymanager.response.JsonResponse;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomElement;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.junit.jupiter.api.*;
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
class GunIntegrationTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WebClient webClient = new WebClient();

    {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }
    private final String baseUrl = "/gun";
    private final String countryUrl = "/country";

    private final CountryDto country1 = CountryDto.builder().id(1L).name("Berdola").build();
    private final CountryDto country2 = CountryDto.builder().id(2L).name("Tekkele").build();

    private final GunDto[] data = {
            GunDto.builder()
                    .designation("Big gun")
                    .caliberInMms(400)
                    .projectileWeightInKgs(500)
                    .rangeInMeters(30000)
                            .country(country1).build(),
            GunDto.builder()
                    .designation("Medium gun")
                    .caliberInMms(200)
                    .projectileWeightInKgs(150)
                    .rangeInMeters(20000)
                    .country(country1).build(),
            GunDto.builder()
                    .designation("Small gun")
                    .caliberInMms(100)
                    .projectileWeightInKgs(15)
                    .rangeInMeters(15000)
                    .country(country2).build(),
            GunDto.builder()
                    .designation("Minuscule gun")
                    .caliberInMms(25)
                    .projectileWeightInKgs(1)
                    .rangeInMeters(5000)
                    .country(country2).build()
    };



    private final GunDto gunToBeAddedFirst =
            GunDto.builder()
                    .designation("Minuscule gun")
                    .caliberInMms(25)
                    .projectileWeightInKgs(1)
                    .rangeInMeters(5000)
                    .country(country2).build();

    private final GunDto gunToBeAddedFirstWithIdToTriggerError =
            GunDto.builder()
                    .id(1L)
                    .designation("Minuscule gun")
                    .caliberInMms(25)
                    .projectileWeightInKgs(1)
                    .rangeInMeters(5000)
                    .country(country2).build();
    private final GunDto gunForUpdatedNoIdLeadsToError =
            GunDto.builder()
                    .designation("Mega gun")
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000)
                    .country(country2).build();
    private final GunDto gunToBeUpdatedInvalidIdLeadsToError =
            GunDto.builder()
                    .id(22L)
                    .designation("Mega gun")
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000)
                    .country(country2).build();
    private final GunDto gunWithUpdateDataValid =
            GunDto.builder()
                    .id(4L)
                    .designation("Mega gun")
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000)
                    .country(country2).build();
    private final GunDto gunNullDesignation =
            GunDto.builder()
                    .id(4L)
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000)
                    .country(country2).build();
    private final GunDto gunEmptyDesignation =
            GunDto.builder()
                    .id(4L)
                    .designation("")
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000)
                    .country(country2).build();


    private final GunDto gunForAdditionMissingNumericalData =
            GunDto.builder()
                    .designation("Mega gun")
                    .country(country1).build();

    private final GunDto gunForUpdateMissingNumericalData =
            GunDto.builder()
                    .id(4L)
                    .designation("Mega gun")
                    .country(country1).build();

    private final GunDto gunForAdditionDesignationAndCountryDuplicated =
            GunDto.builder()
                    .designation("Big gun")
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000)
                    .country(country1).build();

    private final GunDto gunForUpdateDesignationAndCountryDuplicated =
            GunDto.builder()
                    .id(4L)
                    .designation("Big gun")
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000)
                    .country(country1).build();

    private final GunDto gunForAdditionMissingCountry =
            GunDto.builder()
                    .designation("Mega Gun")
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000).build();


    private final GunDto gunForUpdateMissingCountry =
            GunDto.builder()
                    .id(4L)
                    .designation("Mega Gun")
                    .caliberInMms(2500)
                    .projectileWeightInKgs(10000)
                    .rangeInMeters(500000).build();

    private final GunDto gunWithUpdateData =
            GunDto.builder()
                    .id(1L)
                    .designation("Minuscule gun22")
                    .caliberInMms(25)
                    .projectileWeightInKgs(1)
                    .rangeInMeters(5000)
                    .country(country2).build();

    @Test
    @Order(1)
    void postDependencies() {
        HttpEntity<CountryDto> countryHttpEntity = TestUtilities.createHttpEntity(country1);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(countryUrl, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        countryHttpEntity = TestUtilities.createHttpEntity(country2);
        responseEntity =
                testRestTemplate.postForEntity(countryUrl, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    void getAllGunsShouldReturnOkAndEmptyArray() {
        ResponseEntity<GunDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, GunDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(3)
    void postGunShouldReturnOkDuplicateShouldReturnWithError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunToBeAddedFirst);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity =
                testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        gunHttpEntity = TestUtilities.createHttpEntity(gunToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    void getTheAddedGunShouldHaveExpectedData() {
        ResponseEntity<GunDto> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1", GunDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals("Minuscule gun", responseEntity.getBody().getDesignation());
        assertEquals(25, responseEntity.getBody().getCaliberInMms());
        assertEquals(1, responseEntity.getBody().getProjectileWeightInKgs());
        assertEquals(5000, responseEntity.getBody().getRangeInMeters());
        assertEquals(2, responseEntity.getBody().getCountry().getId());
    }

    @Test
    @Order(5)
    void postMultipleGunsThenCheckTheReturnedArrayUsingGunDesignations() {
        HttpEntity<GunDto> gunHttpEntity;
        for (GunDto gunDto : data) {
            if (gunDto.getDesignation().equals("Minuscule Gun"))
                continue;
            gunHttpEntity = TestUtilities.createHttpEntity(gunDto);
            testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        }
        ResponseEntity<GunDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, GunDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustDesignations = Arrays.stream(data).map(GunDto::getDesignation).toList();
        List<String> returnedListJustDesignations =
                Arrays.stream(responseEntity.getBody()).map(GunDto::getDesignation).toList();
        assertTrue(dataAsListJustDesignations.containsAll(returnedListJustDesignations));
    }

    @Test
    @Order(6)
    void updateGunWithNullIdShouldReturnIdError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateGunWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void updateGunIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/22", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void addGunIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunNullDesignation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        gunHttpEntity = TestUtilities.createHttpEntity(gunEmptyDesignation);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void updateGunIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunNullDesignation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        gunHttpEntity = TestUtilities.createHttpEntity(gunEmptyDesignation);
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void addGunIfDesignationAndCountryIsDuplicatedShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunForAdditionDesignationAndCountryDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(12)
    void updateGunIfDesignationAndCountryIsDuplicatedShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunForUpdateDesignationAndCountryDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

   @Test
    @Order(13)
    void deleteGunIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeBadRequest() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(14)
    void addGunIfNumericalDataIsMissingShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunForAdditionMissingNumericalData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(15)
    void updateGunIfNumericalDataIsMissingShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunForUpdateMissingNumericalData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(16)
    void addGunIfCountryIsMissingShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunForAdditionMissingCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(17)
    void updateGunIfCountryIsMissingShouldReturnError() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunForUpdateMissingCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(18)
    void checkIfShowListPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-list-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "List of Guns");
    }

    @Test
    @Order(19)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Add New Gun");
    }

    @Test
    @Order(20)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, gunToBeAddedFirst.getDesignation() + " - Details");
    }

    @Test
    @Order(21)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Update Gun");
    }

    @Test
    @Order(22)
    void updateHullClassificationWithValidDataShouldReturnOk() {
        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gunWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
