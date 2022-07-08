package com.codecool.navymanager.integration_tests;

import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.OfficerDto;
import com.codecool.navymanager.dto.RankDto;
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
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class OfficerIntegrationTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WebClient webClient = new WebClient();

    {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }
    private final String baseUrl = "/officer";
    private final String countryUrl = "/country";

    private final String rankUrl = "/rank";

    private final CountryDto country1 = CountryDto.builder().id(1L).name("Berdola").build();
    private final CountryDto country2 = CountryDto.builder().id(2L).name("Tekkele").build();

    private final RankDto rank1 =
            RankDto.builder().id(1L).precedence(4).designation("Commander").build();

    private final RankDto rank2 =
            RankDto.builder().id(2L).precedence(5).designation("Captain").build();

    private final OfficerDto[] data = {
            OfficerDto.builder()
                    .name("Gdshhd")
                    .dateOfBirth(LocalDate.of(1952, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build(),
            OfficerDto.builder()
                    .name("Hdfsdfsd")
                    .dateOfBirth(LocalDate.of(1922, 11, 11))
                    .rank(rank2)
                    .country(country1)
                    .build(),
            OfficerDto.builder()
                    .name("Fdfsdf")
                    .dateOfBirth(LocalDate.of(1952, 10, 11))
                    .rank(rank1)
                    .country(country2)
                    .build(),
            OfficerDto.builder()
                    .name("Tfsdfsdff")
                    .dateOfBirth(LocalDate.of(1962, 11, 11))
                    .rank(rank2)
                    .country(country2)
                    .build()
    };



    private final OfficerDto officerToBeAddedFirst =
            OfficerDto.builder()
                    .name("Tfsdfsdff")
                    .dateOfBirth(LocalDate.of(1962, 11, 11))
                    .rank(rank2)
                    .country(country2)
                    .build();

    private final OfficerDto officerToBeAddedFirstWithIdToTriggerError =
            OfficerDto.builder()
                    .id(1L)
                    .name("Tfsdfsdff")
                    .dateOfBirth(LocalDate.of(1962, 11, 11))
                    .rank(rank2)
                    .country(country2)
                    .build();
    private final OfficerDto officerForUpdatedNoIdLeadsToError =
            OfficerDto.builder()
                    .name("Gsdfsdklfjs")
                    .dateOfBirth(LocalDate.of(1922, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build();
    private final OfficerDto officerToBeUpdatedInvalidIdLeadsToError =
            OfficerDto.builder()
                    .id(22L)
                    .name("Gsdfsdklfjs")
                    .dateOfBirth(LocalDate.of(1922, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build();
    private final OfficerDto officerWithUpdateDataValid =
            OfficerDto.builder()
                    .id(4L)
                    .name("Gsdfsdklfjs")
                    .dateOfBirth(LocalDate.of(1922, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build();
    private final OfficerDto officerNullName =
            OfficerDto.builder()
                    .id(4L)
                    .dateOfBirth(LocalDate.of(1922, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build();
    private final OfficerDto officerEmptyDesignation =
            OfficerDto.builder()
                    .id(4L)
                    .name("")
                    .dateOfBirth(LocalDate.of(1922, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build();
    private final OfficerDto officerForAdditionMissingData =
            OfficerDto.builder()
                    .country(country1).build();

    private final OfficerDto officerForUpdateMissingData =
            OfficerDto.builder()
                    .id(4L)
                    .country(country1).build();

    private final OfficerDto officerWithUpdateData =
            OfficerDto.builder()
                    .id(1L)
                    .name("Sfsdfss")
                    .dateOfBirth(LocalDate.of(1955, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build();

    @Test
    @Order(1)
    void postDependencies() {
        HttpEntity<CountryDto> countryHttpEntity = Utils.createHttpEntity(country1);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(countryUrl, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        countryHttpEntity = Utils.createHttpEntity(country2);
        responseEntity =
                testRestTemplate.postForEntity(countryUrl, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpEntity<RankDto> rankHttpEntity =
                Utils.createHttpEntity(rank1);
        responseEntity =
                testRestTemplate.postForEntity(rankUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        rankHttpEntity =
                Utils.createHttpEntity(rank2);
        responseEntity =
                testRestTemplate.postForEntity(rankUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    void getAllOfficersShouldReturnOkAndEmptyArray() {
        ResponseEntity<OfficerDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, OfficerDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(3)
    void postOfficerShouldReturnOkDuplicateShouldReturnWithError() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerToBeAddedFirst);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        officerHttpEntity = Utils.createHttpEntity(officerToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    void getTheAddedOfficerShouldHaveExpectedData() {
        ResponseEntity<OfficerDto> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1", OfficerDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals("Tfsdfsdff", responseEntity.getBody().getName());
        assertEquals(LocalDate.of(1962, 11, 11), responseEntity.getBody().getDateOfBirth());
        assertEquals(2, responseEntity.getBody().getRank().getId());
        assertEquals(2, responseEntity.getBody().getCountry().getId());
    }

    @Test
    @Order(5)
    void postMultipleOfficersThenCheckTheReturnedArrayUsingOfficerDesignations() {
        HttpEntity<OfficerDto> officerHttpEntity;
        for (OfficerDto officerDto : data) {
            if (officerDto.getName().equals("Tfsdfsdff"))
                continue;
            officerHttpEntity = Utils.createHttpEntity(officerDto);
            testRestTemplate.postForEntity(baseUrl, officerHttpEntity, JsonResponse.class);
        }
        ResponseEntity<OfficerDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, OfficerDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustNames = Arrays.stream(data).map(OfficerDto::getName).toList();
        List<String> returnedListJustNames =
                Arrays.stream(responseEntity.getBody()).map(OfficerDto::getName).toList();
        assertTrue(dataAsListJustNames.containsAll(returnedListJustNames));
    }

    @Test
    @Order(6)
    void updateOfficerWithNullIdShouldReturnIdError() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateOfficerWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void updateOfficerIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/22", HttpMethod.PUT, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void addOfficerIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        officerHttpEntity = Utils.createHttpEntity(officerEmptyDesignation);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void updateOfficerIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        officerHttpEntity = Utils.createHttpEntity(officerEmptyDesignation);
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

   @Test
    @Order(13)
    void deleteOfficerIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeError() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(14)
    void addOfficerIfNumericalDataIsMissingShouldReturnError() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerForAdditionMissingData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(15)
    void updateOfficerIfNumericalDataIsMissingShouldReturnError() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerForUpdateMissingData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, officerHttpEntity, JsonResponse.class);
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
        assertEquals(h2FoundTextContent, "List of Officers");
    }

    @Test
    @Order(19)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Add New Officer");
    }

    @Test
    @Order(20)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, officerToBeAddedFirst.getName() + " - Details");
    }

    @Test
    @Order(21)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Update Officer");
    }

    @Test
    @Order(22)
    void updateHullClassificationWithValidDataShouldReturnOk() {
        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officerWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
