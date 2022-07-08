package com.codecool.navymanager.integration_tests;

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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class RankIntegrationTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WebClient webClient = new WebClient();

    {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }
    private final String baseUrl = "/rank";

    private final RankDto[] data = {
            RankDto.builder().precedence(1).designation("Ensign").build(),
            RankDto.builder().precedence(2).designation("Sublieutenant").build(),
            RankDto.builder().precedence(3).designation("Lieutenant Commander").build(),
            RankDto.builder().precedence(4).designation("Commander").build()
    };

    private final RankDto rankToBeAddedFirst =
            RankDto.builder().precedence(4).designation("Commander").build();

    private final RankDto rankToBeAddedFirstWithIdToTriggerError =
            RankDto.builder().id(1L).precedence(4).designation("Commander").build();
    private final RankDto rankForUpdatedNoIdLeadsToError =
            RankDto.builder().precedence(5).designation("Captain").build();
    private final RankDto rankToBeUpdatedInvalidIdLeadsToError =
            RankDto.builder().id(22L).precedence(5).designation("Captain").build();
    private final RankDto rankWithUpdateDataValid =
            RankDto.builder().id(4L).precedence(5).designation("Captain").build();
    private final RankDto rankNullDesignation =
            RankDto.builder().id(4L).precedence(5).build();
    private final RankDto rankEmptyDesignation =
            RankDto.builder().id(4L).precedence(5).designation("").build();

    private final RankDto rankToAddWithDuplicatePrecedenceLeadsToError =
            RankDto.builder().precedence(4).designation("Captain").build();
    private final RankDto rankToUpdateWithDuplicatePrecedenceLeadsToError =
            RankDto.builder().id(4L).precedence(4).designation("Captain").build();

    private final RankDto rankForAdditionDesignationDuplicated =
            RankDto.builder().precedence(5).designation("Ensign").build();

    private final RankDto rankForUpdateDesignationDuplicated =
            RankDto.builder().id(4L).precedence(5).designation("Ensign").build();

    private final RankDto rankWithUpdateData =
            RankDto.builder().id(1L).precedence(4).designation("Comm22").build();

    @Test
    @Order(1)
    void getAllRanksShouldReturnOkAndEmptyArray() {
        ResponseEntity<RankDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, RankDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(2)
    void postRankShouldReturnOkDuplicateShouldReturnWithError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankToBeAddedFirst);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity =
                testRestTemplate.postForEntity(baseUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        rankHttpEntity = Utils.createHttpEntity(rankToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(3)
    void getTheAddedRankShouldHaveIdOfOneAndDesignationCommanderAndPrecedenceFour() {
        ResponseEntity<RankDto> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1", RankDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, responseEntity.getBody().getId());
        assertEquals("Commander", responseEntity.getBody().getDesignation());
        assertEquals(4, responseEntity.getBody().getPrecedence());
    }

    @Test
    @Order(4)
    void postMultipleRanksThenCheckTheReturnedArrayUsingRankDesignations() {
        HttpEntity<RankDto> rankHttpEntity;
        for (RankDto rankDto : data) {
            if (rankDto.getDesignation().equals("Commander"))
                continue;
            rankHttpEntity = Utils.createHttpEntity(rankDto);
            testRestTemplate.postForEntity(baseUrl, rankHttpEntity, JsonResponse.class);
        }
        ResponseEntity<RankDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, RankDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustDesignations = Arrays.stream(data).map(RankDto::getDesignation).toList();
        List<String> returnedListJustDesignations =
                Arrays.stream(responseEntity.getBody()).map(RankDto::getDesignation).toList();
        assertTrue(dataAsListJustDesignations.containsAll(returnedListJustDesignations));
    }

    @Test
    @Order(5)
    void updateRankWithNullIdShouldReturnIdError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(6)
    void updateRankWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateRankIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/22", HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void addRankIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankNullDesignation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        rankHttpEntity = Utils.createHttpEntity(rankEmptyDesignation);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void updateRankIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankNullDesignation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        rankHttpEntity = Utils.createHttpEntity(rankEmptyDesignation);
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void addRankIfPrecedenceIsDuplicatedShouldReturnError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankToAddWithDuplicatePrecedenceLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, rankHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void updateRankIfPrecedenceIsDuplicatedShouldReturnError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankToUpdateWithDuplicatePrecedenceLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(12)
    void addRankIfDesignationIsDuplicatedShouldReturnError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankForAdditionDesignationDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, rankHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(13)
    void updateRankIfDesignationIsDuplicatedShouldReturnError() {
        HttpEntity<RankDto> rankHttpEntity = Utils.createHttpEntity(rankForUpdateDesignationDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

   @Test
    @Order(14)
    void deleteRankIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeBadRequest() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(15)
    void checkIfShowListPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-list-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "List of Ranks");
    }

    @Test
    @Order(16)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Add New Rank");
    }

    @Test
    @Order(17)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, rankToBeAddedFirst.getDesignation() + " - Details");
    }

    @Test
    @Order(18)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Update Rank");
    }

    @Test
    @Order(19)
    void updateRankWithValidDataShouldReturnOk() {
        HttpEntity<RankDto> rankHttpEntity =
                Utils.createHttpEntity(rankWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
