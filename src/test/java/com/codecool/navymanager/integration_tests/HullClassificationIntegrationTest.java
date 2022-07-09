package com.codecool.navymanager.integration_tests;

import com.codecool.navymanager.dto.HullClassificationDto;
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
class HullClassificationIntegrationTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WebClient webClient = new WebClient();

    {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }
    private final String baseUrl = "/hull-classification";

    private final String baseUrlApi = "/hull-classification/api";

    private final HullClassificationDto[] data = {
            HullClassificationDto.builder().abbreviation("DD").designation("Destroyer").build(),
            HullClassificationDto.builder().abbreviation("CL").designation("Light Cruiser").build(),
            HullClassificationDto.builder().abbreviation("CA").designation("Heavy Cruiser").build(),
            HullClassificationDto.builder().abbreviation("BB").designation("Battleship").build()
    };

    private final HullClassificationDto hullClassificationToBeAddedFirst =
            HullClassificationDto.builder().abbreviation("BB").designation("Battleship").build();

    private final HullClassificationDto hullClassificationToBeAddedFirstWithIdToTriggerError =
            HullClassificationDto.builder().id(1L).abbreviation("BB").designation("Battleship").build();
    private final HullClassificationDto hullClassificationForUpdatedNoIdLeadsToError =
            HullClassificationDto.builder().abbreviation("CV").designation("Aircraft Carrier").build();
    private final HullClassificationDto hullClassificationToBeUpdatedInvalidIdLeadsToError =
            HullClassificationDto.builder().id(22L).abbreviation("CV").designation("Aircraft Carrier").build();
    private final HullClassificationDto hullClassificationWithUpdateDataValid =
            HullClassificationDto.builder().id(4L).abbreviation("CV").designation("Aircraft Carrier").build();
    private final HullClassificationDto hullClassificationNullAbbreviation =
            HullClassificationDto.builder().id(4L).designation("Aircraft Carrier").build();
    private final HullClassificationDto hullClassificationEmptyAbbreviation =
            HullClassificationDto.builder().id(4L).abbreviation("").designation("Aircraft Carrier").build();

    private final HullClassificationDto hullClassificationNullDesignation =
            HullClassificationDto.builder().id(4L).designation("Aircraft Carrier").build();
    private final HullClassificationDto hullClassificationEmptyDesignation =
            HullClassificationDto.builder().id(4L).abbreviation("").designation("Aircraft Carrier").build();

    private final HullClassificationDto hullClassificationForAdditionAbbreviationDuplicated =
            HullClassificationDto.builder().abbreviation("BB").designation("Aircraft Carrier").build();
    private final HullClassificationDto hullClassificationForUpdateAbbreviationDuplicated =
            HullClassificationDto.builder().id(4L).abbreviation("CA").designation("Aircraft Carrier").build();

    private final HullClassificationDto hullClassificationForAdditionDesignationDuplicated =
            HullClassificationDto.builder().abbreviation("CVL").designation("Destroyer").build();
    private final HullClassificationDto hullClassificationForUpdateDesignationDuplicated =
            HullClassificationDto.builder().id(4L).abbreviation("BB").designation("Destroyer").build();

    private final HullClassificationDto hullClassificationWithUpdateData =
            HullClassificationDto.builder().id(1L).abbreviation("BB").designation("Battleship22").build();

    @Test
    @Order(1)
    void getAllHullClassificationsShouldReturnOkAndEmptyArray() {
        ResponseEntity<HullClassificationDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrlApi, HullClassificationDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(2)
    void postHullClassificationShouldReturnOkDuplicateShouldReturnWithError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationToBeAddedFirst);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        hullClassificationHttpEntity = Utils.createHttpEntity(hullClassificationToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    void getTheAddedHullClassificationShouldHaveIdOfOneAndDesignationBattleshipAndAbbreviationBB() {
        ResponseEntity<HullClassificationDto> responseEntity =
                testRestTemplate.getForEntity(baseUrlApi + "/1", HullClassificationDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getId());
        assertEquals("Battleship", responseEntity.getBody().getDesignation());
        assertEquals("BB", responseEntity.getBody().getAbbreviation());
    }

    @Test
    @Order(4)
    void postMultipleHullClassificationsThenCheckTheReturnedArrayUsingHullClassificationDesignations() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity;
        for (HullClassificationDto hullClassificationDto : data) {
            if (hullClassificationDto.getDesignation().equals("Battleship"))
                continue;
            hullClassificationHttpEntity = Utils.createHttpEntity(hullClassificationDto);
            testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        }
        ResponseEntity<HullClassificationDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrlApi, HullClassificationDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustDesignations = Arrays.stream(data).map(HullClassificationDto::getDesignation).toList();
        List<String> returnedListJustDesignations =
                Arrays.stream(responseEntity.getBody()).map(HullClassificationDto::getDesignation).toList();
        assertTrue(dataAsListJustDesignations.containsAll(returnedListJustDesignations));
    }

    @Test
    @Order(5)
    void updateHullClassificationWithNullIdShouldReturnIdError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(6)
    void updateHullClassificationWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/1", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateHullClassificationIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/22", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void addHullClassificationIfAbbreviationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationNullAbbreviation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        hullClassificationHttpEntity = Utils.createHttpEntity(hullClassificationEmptyAbbreviation);
        responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void updateHullClassificationIfAbbreviationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationNullAbbreviation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        hullClassificationHttpEntity = Utils.createHttpEntity(hullClassificationEmptyAbbreviation);
        responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void addHullClassificationIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationNullDesignation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        hullClassificationHttpEntity = Utils.createHttpEntity(hullClassificationEmptyDesignation);
        responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void updateHullClassificationIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationNullDesignation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        hullClassificationHttpEntity = Utils.createHttpEntity(hullClassificationEmptyDesignation);
        responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(12)
    void addHullClassificationIfAbbreviationIsDuplicatedShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationForAdditionAbbreviationDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(13)
    void updateHullClassificationIfPrecedenceIsDuplicatedShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationForUpdateAbbreviationDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(14)
    void addHullClassificationIfDesignationIsDuplicatedShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationForAdditionDesignationDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlApi, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(15)
    void updateHullClassificationIfDesignationIsDuplicatedShouldReturnError() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationForUpdateDesignationDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(16)
    void deleteHullClassificationIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeBadRequest() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(17)
    void checkIfShowListPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-list-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "List of Hull Classifications");
    }

    @Test
    @Order(18)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Add New Hull Classification");
    }

    @Test
    @Order(19)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, hullClassificationToBeAddedFirst.getDesignation() + " - Details");
    }

    @Test
    @Order(20)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Update Hull Classification");
    }

    @Test
    @Order(21)
    void updateHullClassificationWithValidDataShouldReturnOk() {
        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassificationWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlApi + "/1", HttpMethod.PUT, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
