package com.codecool.navymanager.integration_tests;

import com.codecool.navymanager.dto.*;
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
class ShipIntegrationTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WebClient webClient = new WebClient();

    {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }

    private final String baseUrl = "/ship";
    private final String rankUrl = "/rank";
    private final String shipClassUrl = "/ship-class";
    private final String countryUrl = "/country";
    private final String officerUrl = "/officer";
    private final String hullClassificationUrl = "/hull-classification";

    private final CountryDto country1 = CountryDto.builder().id(1L).name("Berdola").build();
    private final CountryDto country2 = CountryDto.builder().id(2L).name("Tekkele").build();

    private final HullClassificationDto hullClassification1 =
            HullClassificationDto.builder().id(1L).abbreviation("CC").designation("Circular Cruiser").build();
    private final HullClassificationDto hullClassification2 =
            HullClassificationDto.builder().id(2L).abbreviation("FF").designation("Feeble Frigate").build();

    private final ShipClassDto shipClass1 =
            ShipClassDto.builder()
                    .id(1L)
                    .name("Alaska")
                    .displacementInTons(33333)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(33)
                    .armorDeckInCms(12)
                    .armorTurretInCms(35)
                    .speedInKmh(50)
                    .country(country1).build();

    private final ShipClassDto shipClass2 =
            ShipClassDto.builder()
                    .id(2L)
                    .name("Beefy")
                    .displacementInTons(31333)
                    .hullClassification(hullClassification2)
                    .armorBeltInCms(37)
                    .armorDeckInCms(13)
                    .armorTurretInCms(31)
                    .speedInKmh(51)
                    .country(country2).build();

    private final ShipClassDto shipClass3DoesNotExist =
            ShipClassDto.builder()
                    .id(55L)
                    .name("Beefy22")
                    .displacementInTons(31333)
                    .hullClassification(hullClassification2)
                    .armorBeltInCms(37)
                    .armorDeckInCms(13)
                    .armorTurretInCms(31)
                    .speedInKmh(51)
                    .country(country1).build();

    private final ShipClassDto shipClass2CountryMismatch =
            ShipClassDto.builder()
                    .id(2L)
                    .name("Beefy")
                    .displacementInTons(31333)
                    .hullClassification(hullClassification2)
                    .armorBeltInCms(37)
                    .armorDeckInCms(13)
                    .armorTurretInCms(31)
                    .speedInKmh(51)
                    .country(country1).build();

    private final RankDto rank1 =
            RankDto.builder().id(1L).precedence(4).designation("Commander").build();

    private final OfficerDto officer1 =
            OfficerDto.builder()
                    .id(1L)
                    .name("Colonel Stuart")
                    .dateOfBirth(LocalDate.of(1950, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build();

    private final OfficerDto officer2 =
            OfficerDto.builder()
                    .id(2L)
                    .name("Maleficent")
                    .dateOfBirth(LocalDate.of(1960, 11, 11))
                    .rank(rank1)
                    .country(country2)
                    .build();

    private final OfficerDto officer3 =
            OfficerDto.builder()
                    .id(3L)
                    .name("Fleety")
                    .dateOfBirth(LocalDate.of(1962, 11, 11))
                    .rank(rank1)
                    .country(country1)
                    .build();

    private final OfficerDto officer5 =
            OfficerDto.builder()
                    .id(5L)
                    .name("Flat")
                    .dateOfBirth(LocalDate.of(1942, 11, 11))
                    .rank(rank1)
                    .country(country2)
                    .build();

    private final OfficerDto officer4FromCountry2 =
            OfficerDto.builder()
                    .id(4L)
                    .name("MacMahon")
                    .dateOfBirth(LocalDate.of(1952, 11, 11))
                    .rank(rank1)
                    .country(country2)
                    .build();

    private final ShipDto ship1 =
            ShipDto.builder()
                    .name("Mariposa")
                    .shipClass(shipClass2)
                    .captain(officer2)
                    .country(country2)
                    .build();

    private final IdentityDto withIdOfOne =
            IdentityDto.builder().id(1L).build();

    private final IdentityDto withIdOfTwo =
            IdentityDto.builder().id(2L).build();

    private final ShipDto ship2CaptainFromAnotherCountry =
            ShipDto.builder()
                    .name("Cockroach")
                    .shipClass(shipClass2)
                    .captain(officer2)
                    .country(country1)
                    .build();

    private final ShipDto shipInvalidData =
            ShipDto.builder()
                    .name("Cockroach")
                    .shipClass(shipClass3DoesNotExist)
                    .country(country1)
                    .build();
    private final ShipDto[] data = {
            ShipDto.builder()
                    .name("Menethil")
                    .shipClass(shipClass1)
                    .country(country1)
                    .build(),
            ShipDto.builder()
                    .name("Kotten")
                    .shipClass(shipClass1)
                    .country(country1)
                    .build(),
            ShipDto.builder()
                    .name("Abrupt")
                    .shipClass(shipClass2)
                    .country(country2)
                    .build(),
            ShipDto.builder()
                    .name("Efreet")
                    .shipClass(shipClass2)
                    .captain(officer2)
                    .country(country2)
                    .build(),
    };


    private final ShipDto shipToBeAddedFirst =
            ShipDto.builder()
                    .name("Efreet")
                    .captain(officer2)
                    .shipClass(shipClass2)
                    .country(country2)
                    .build();

    private final ShipDto shipToBeAddedFirstCountryMismatchThroughShipClass =
            ShipDto.builder()
                    .name("Efreet")
                    .shipClass(shipClass2CountryMismatch)
                    .country(country1)
                    .build();

    private final ShipDto shipToBeAddedFirstWithIdToTriggerError =
            ShipDto.builder()
                    .id(1L)
                    .name("Efreet")
                    .shipClass(shipClass2)
                    .country(country2)
                    .build();
    private final ShipDto shipForUpdatedNoIdLeadsToError =
            ShipDto.builder()
                    .name("Tangled")
                    .shipClass(shipClass2)
                    .country(country2)
                    .build();
    private final ShipDto shipToBeUpdatedInvalidIdLeadsToError =
            ShipDto.builder()
                    .id(22L)
                    .name("Tangled")
                    .shipClass(shipClass2)
                    .country(country2)
                    .build();
    private final ShipDto shipWithUpdateDataValid =
            ShipDto.builder()
                    .id(4L)
                    .name("Tangled")
                    .shipClass(shipClass2)
                    .country(country2)
                    .build();
    private final ShipDto shipNullName =
            ShipDto.builder()
                    .id(4L)
                    .shipClass(shipClass2)
                    .country(country2)
                    .build();
    private final ShipDto shipEmptyName =
            ShipDto.builder()
                    .id(4L)
                    .name("")
                    .shipClass(shipClass2)
                    .country(country2)
                    .build();

    private final ShipDto shipForAdditionMissingData =
            ShipDto.builder()
                    .country(country2)
                    .build();

    private final ShipDto shipForUpdateMissingData =
            ShipDto.builder()
                    .id(4L)
                    .country(country2)
                    .build();

    private final ShipDto shipForAdditionNameAndShipClassDuplicated =
            ShipDto.builder()
                    .name("Kotten")
                    .shipClass(shipClass1)
                    .country(country1)
                    .build();

    private final ShipDto shipForUpdateNameAndShipClassDuplicated =
            ShipDto.builder()
                    .id(2L)
                    .name("Kotten")
                    .shipClass(shipClass1)
                    .country(country1)
                    .build();

    private final ShipDto shipForAdditionMissingCountry =
            ShipDto.builder()
                    .name("Tangled")
                    .shipClass(shipClass2)
                    .build();


    private final ShipDto shipForUpdateMissingCountry =
            ShipDto.builder()
                    .id(4L)
                    .name("Tangled")
                    .shipClass(shipClass2)
                    .build();

    private final ShipDto shipWithUpdateData =
            ShipDto.builder()
                    .id(1L)
                    .name("Tangled22")
                    .captain(officer5)
                    .shipClass(shipClass2)
                    .country(country2)
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

        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassification1);
        responseEntity =
                testRestTemplate.postForEntity(hullClassificationUrl, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        hullClassificationHttpEntity =
                Utils.createHttpEntity(hullClassification2);
        responseEntity =
                testRestTemplate.postForEntity(hullClassificationUrl, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClass1);
        responseEntity =
                testRestTemplate.postForEntity(shipClassUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        shipClassHttpEntity =
                Utils.createHttpEntity(shipClass2);
        responseEntity =
                testRestTemplate.postForEntity(shipClassUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpEntity<RankDto> rankHttpEntity =
                Utils.createHttpEntity(rank1);
        responseEntity =
                testRestTemplate.postForEntity(rankUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpEntity<OfficerDto> officerHttpEntity =
                Utils.createHttpEntity(officer1);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        officerHttpEntity =
                Utils.createHttpEntity(officer2);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        officerHttpEntity =
                Utils.createHttpEntity(officer3);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        officerHttpEntity =
                Utils.createHttpEntity(officer4FromCountry2);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        officerHttpEntity =
                Utils.createHttpEntity(officer5);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    void getAllShipsShouldReturnOkAndEmptyArray() {
        ResponseEntity<ShipDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, ShipDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(3)
    void postShipShouldReturnOkDuplicateShouldReturnWithErrorCountryMismatchShouldReturnError() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipToBeAddedFirstCountryMismatchThroughShipClass);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

        shipHttpEntity =
                Utils.createHttpEntity(shipToBeAddedFirst);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

        shipHttpEntity = Utils.createHttpEntity(shipToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    void getTheAddedShipShouldHaveExpectedData() {
        ResponseEntity<ShipDto> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1", ShipDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals("Efreet", responseEntity.getBody().getName());
        assertEquals(2, responseEntity.getBody().getShipClass().getId());
        assertEquals(2, responseEntity.getBody().getCountry().getId());
    }

    @Test
    @Order(5)
    void postMultipleShipsThenCheckTheReturnedArrayUsingShipNames() {
        HttpEntity<ShipDto> shipHttpEntity;
        for (ShipDto shipDto : data) {
            if (shipDto.getName().equals("Efreet"))
                continue;
            shipHttpEntity = Utils.createHttpEntity(shipDto);
            ResponseEntity<JsonResponse> responseEntity =
                    testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        }
        ResponseEntity<ShipDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, ShipDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustDesignations = Arrays.stream(data).map(ShipDto::getName).toList();
        List<String> returnedListJustDesignations =
                Arrays.stream(responseEntity.getBody()).map(ShipDto::getName).toList();
        assertTrue(dataAsListJustDesignations.containsAll(returnedListJustDesignations));
    }

    @Test
    @Order(6)
    void updateShipWithNullIdShouldReturnIdError() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateShipWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void updateShipIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/22", HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void addShipIfNameIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        shipHttpEntity = Utils.createHttpEntity(shipEmptyName);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void updateShipIfNameIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        shipHttpEntity = Utils.createHttpEntity(shipEmptyName);
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void addShipIfNameAndShipClassIsDuplicatedShouldReturnError() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipForAdditionNameAndShipClassDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(12)
    void updateShipIfNameAndShipClassIsDuplicatedShouldReturnError() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipForUpdateNameAndShipClassDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/2", HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(13)
    void deleteShipIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeError() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(14)
    void findByIdShouldReturnWithErrorForNonexistent() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/22", JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(15)
    public void addWithCaptainPostedOnAnotherShip() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(ship1);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

    }

    @Test
    @Order(16)
    public void addWithCaptainFromDifferentCountry() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(ship2CaptainFromAnotherCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

    }

    @Test
    @Order(16)
    public void addWithNonexistentShipClass() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipInvalidData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
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
        assertEquals(h2FoundTextContent, "List of Ships");
    }

    @Test
    @Order(18)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Add New Ship");
    }

    @Test
    @Order(19)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, shipToBeAddedFirst.getName() + " - Details");
    }

    @Test
    @Order(20)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(h2FoundTextContent, "Update Ship");
    }

    @Test
    @Order(21)
    void updateShipWithValidDataShouldReturnOkNewOfficer() {
        HttpEntity<ShipDto> shipHttpEntity =
                Utils.createHttpEntity(shipWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}
