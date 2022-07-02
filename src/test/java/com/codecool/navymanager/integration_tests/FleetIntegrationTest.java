package com.codecool.navymanager.integration_tests;

import com.codecool.navymanager.TestUtilities;
import com.codecool.navymanager.dto.*;
import com.codecool.navymanager.response.JsonResponse;
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
class FleetIntegrationTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WebClient webClient = new WebClient();

    {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }
    private final String baseUrl = "/fleet";
    private final String shipUrl = "/ship";
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
                    .country(country1).build();

    private final RankDto rank1 =
            RankDto.builder().id(1L).precedence(4).designation("Commander").build();

    private final OfficerDto officer1 =
            OfficerDto.builder()
                    .id(1L)
                    .name("Colonel Stuart")
                    .dateOfBirth(LocalDate.of(1950,11,11))
                    .rank(rank1)
                    .country(country1)
                    .build();

    private final OfficerDto officer2 =
            OfficerDto.builder()
                    .id(2L)
                    .name("Maleficent")
                    .dateOfBirth(LocalDate.of(1960,11,11))
                    .rank(rank1)
                    .country(country1)
                    .build();

    private final OfficerDto officer3 =
            OfficerDto.builder()
                    .id(3L)
                    .name("Fleety")
                    .dateOfBirth(LocalDate.of(1962,11,11))
                    .rank(rank1)
                    .country(country1)
                    .build();

    private final OfficerDto officer5 =
            OfficerDto.builder()
                    .id(5L)
                    .name("Flat")
                    .dateOfBirth(LocalDate.of(1942,11,11))
                    .rank(rank1)
                    .country(country1)
                    .build();

    private final OfficerDto officer4FromCountry2 =
            OfficerDto.builder()
                    .id(4L)
                    .name("MacMahon")
                    .dateOfBirth(LocalDate.of(1952,11,11))
                    .rank(rank1)
                    .country(country2)
                    .build();
    private final ShipDto ship1 =
            ShipDto.builder()
                    .name("Mariposa")
                    .shipClass(shipClass1)
                    .captain(officer1)
                    .country(country1)
                    .build();

    private final IdentityDto withIdOfOne =
            IdentityDto.builder().id(1L).build();

    private final IdentityDto withIdOfTwo =
            IdentityDto.builder().id(2L).build();

    private final ShipDto ship2 =
            ShipDto.builder()
                    .name("Cockroach")
                    .shipClass(shipClass2)
                    .captain(officer2)
                    .country(country1)
                    .build();

    private final ShipDto shipInvalidData =
            ShipDto.builder().build();

    private final FleetDto[] data = {
            FleetDto.builder()
                    .designation("Fleet Quartz")
                    .country(country1).build(),
            FleetDto.builder()
                    .designation("Fleet Corund")
                    .country(country2).build(),
            FleetDto.builder()
                    .designation("Fleet Ruby")
                    .country(country2).build(),
            FleetDto.builder()
                    .designation("Fleet Diamond")
                    .country(country1).build()
    };

    private final FleetDto fleetToBeAddedFirst =
            FleetDto.builder()
                    .commander(officer3)
                    .designation("Fleet Diamond")
                    .country(country1).build();

    private final FleetDto fleetToBeAddedOfficerUnavailable =
            FleetDto.builder()
                    .commander(officer3)
                    .designation("Sad Panda")
                    .country(country1).build();

    private final FleetDto fleetToBeUpdatedOfficerUnavailable =
            FleetDto.builder()
                    .id(3L)
                    .commander(officer3)
                    .designation("Sad Panda")
                    .country(country1).build();

    private final FleetDto fleetUpdateWithCommanderFromDifferentCountry =
            FleetDto.builder()
                    .id(1L)
                    .commander(officer4FromCountry2)
                    .designation("Fleet Diamond")
                    .country(country1).build();

    private final FleetDto fleetToBeAddedFirstWithIdToTriggerError =
            FleetDto.builder()
                    .id(1L)
                    .commander(officer3)
                    .designation("Fleet Diamond")
                    .country(country1).build();
    private final FleetDto fleetForUpdatedNoIdLeadsToError =
            FleetDto.builder()
                    .designation("Fleet Krrik")
                    .country(country1).build();
    private final FleetDto fleetToBeUpdatedInvalidIdLeadsToError =
            FleetDto.builder()
                    .id(22L)
                    .designation("Fleet Krrik")
                    .country(country1).build();
    private final FleetDto fleetWithUpdateDataValid =
            FleetDto.builder()
                    .id(2L)
                    .designation("Fleet Krrik")
                    .country(country1).build();

    private final FleetDto fleetNullDesignation =
            FleetDto.builder()
                    .id(2L)
                    .country(country1).build();

    private final FleetDto fleetEmptyDesignation =
            FleetDto.builder()
                    .id(2L)
                    .designation("")
                    .country(country1).build();
    private final FleetDto fleetForAdditionDesignationAndCountryDuplicated =
            FleetDto.builder()
                    .designation("Fleet Diamond")
                    .country(country1).build();

    private final FleetDto fleetForUpdateDesignationAndCountryDuplicated =
            FleetDto.builder()
                    .id(4L)
                    .designation("Fleet Diamond")
                    .country(country1).build();

    private final FleetDto fleetForAdditionMissingCountry =
            FleetDto.builder()
                    .designation("Fleet Malik").build();

    private final FleetDto fleetForUpdateMissingCountry =
            FleetDto.builder()
                    .id(2L)
                    .designation("Fleet Malik").build();

    private final FleetDto fleetWithUpdateData =
            FleetDto.builder()
                    .id(3L)
                    .commander(officer5)
                    .designation("Fleet Termagant")
                    .country(country1).build();

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

        HttpEntity<HullClassificationDto> hullClassificationHttpEntity =
                TestUtilities.createHttpEntity(hullClassification1);
        responseEntity =
                testRestTemplate.postForEntity(hullClassificationUrl, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        hullClassificationHttpEntity =
                TestUtilities.createHttpEntity(hullClassification2);
        responseEntity =
                testRestTemplate.postForEntity(hullClassificationUrl, hullClassificationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClass1);
        responseEntity =
                testRestTemplate.postForEntity(shipClassUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClass2);
        responseEntity =
                testRestTemplate.postForEntity(shipClassUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpEntity<RankDto> rankHttpEntity =
                TestUtilities.createHttpEntity(rank1);
        responseEntity =
                testRestTemplate.postForEntity(rankUrl, rankHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpEntity<OfficerDto> officerHttpEntity =
                TestUtilities.createHttpEntity(officer1);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

       officerHttpEntity =
                TestUtilities.createHttpEntity(officer2);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        officerHttpEntity =
                TestUtilities.createHttpEntity(officer3);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        officerHttpEntity =
                TestUtilities.createHttpEntity(officer4FromCountry2);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        officerHttpEntity =
                TestUtilities.createHttpEntity(officer5);
        responseEntity =
                testRestTemplate.postForEntity(officerUrl, officerHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        HttpEntity<ShipDto> shipHttpEntity =
                TestUtilities.createHttpEntity(ship1);
        responseEntity =
                testRestTemplate.postForEntity(shipUrl, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        shipHttpEntity =
                TestUtilities.createHttpEntity(ship2);
        responseEntity =
                testRestTemplate.postForEntity(shipUrl, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    void getAllFleetsShouldReturnOkAndEmptyArray() {
        ResponseEntity<FleetDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, FleetDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(3)
    void postFleetShouldReturnOkDuplicateShouldReturnWithError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetToBeAddedFirst);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity =
                testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());

        fleetHttpEntity = TestUtilities.createHttpEntity(fleetToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    void getTheAddedFleetShouldHaveExpectedData() {
        ResponseEntity<FleetDto> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1", FleetDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals("Fleet Diamond", responseEntity.getBody().getDesignation());
        assertEquals(1L, responseEntity.getBody().getCountry().getId());
    }

    @Test
    @Order(5)
    void postMultipleFleetsThenCheckTheReturnedArrayUsingFleetDesignations() {
        HttpEntity<FleetDto> fleetHttpEntity;
        for (FleetDto fleetDto : data) {
            if (fleetDto.getDesignation().equals("Fleet Diamond"))
                continue;
            fleetHttpEntity = TestUtilities.createHttpEntity(fleetDto);
            testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        }
        ResponseEntity<FleetDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, FleetDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustDesignations = Arrays.stream(data).map(FleetDto::getDesignation).toList();
        List<String> returnedListJustDesignations =
                Arrays.stream(responseEntity.getBody()).map(FleetDto::getDesignation).toList();
        assertTrue(dataAsListJustDesignations.containsAll(returnedListJustDesignations));
    }

    @Test
    @Order(6)
    void updateFleetWithNullIdShouldReturnIdError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/2", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateFleetWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void updateFleetIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/22", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void updateFleetWithCommanderFromAnotherCountryShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetUpdateWithCommanderFromDifferentCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void updateFleetWithCommanderUnavailableShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetToBeUpdatedOfficerUnavailable);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/3", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void addFleetIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetNullDesignation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        fleetHttpEntity = TestUtilities.createHttpEntity(fleetEmptyDesignation);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void updateFleetIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetNullDesignation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/2", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        fleetHttpEntity = TestUtilities.createHttpEntity(fleetEmptyDesignation);
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/2", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void addFleetIfDesignationAndCountryIsDuplicatedShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetForAdditionDesignationAndCountryDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void addFleetIfOfficerUnavailableReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetToBeAddedOfficerUnavailable);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(12)
    void updateFleetIfDesignationAndCountryIsDuplicatedShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetForUpdateDesignationAndCountryDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        ResponseEntity<FleetDto[]> responseEntityForList =
                testRestTemplate.getForEntity(baseUrl, FleetDto[].class);
        assertEquals(HttpStatus.OK, responseEntityForList.getStatusCode());
        System.out.println(Arrays.toString(responseEntityForList.getBody()));
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(13)
    void deleteFleetIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeBadRequest() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(14)
    void addFleetIfCountryIsMissingShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetForAdditionMissingCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, fleetHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(15)
    void updateFleetIfCountryIsMissingShouldReturnError() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetForUpdateMissingCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
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
        assertEquals("List of Fleets", h2FoundTextContent);
    }

    @Test
    @Order(18)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Add New Fleet", h2FoundTextContent);
    }

    @Test
    @Order(19)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(fleetToBeAddedFirst.getDesignation() + " - Details", h2FoundTextContent);
    }

    @Test
    @Order(20)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Update Fleet", h2FoundTextContent);
    }

    @Test
    @Order(21)
    void updateFleetWithValidDataShouldReturnOk() {
        HttpEntity<FleetDto> fleetHttpEntity =
                TestUtilities.createHttpEntity(fleetWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/3", HttpMethod.PUT, fleetHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(22)
    void checkIfShowAddShipFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/ship/show-add-ship-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Add New Ship to " + fleetToBeAddedFirst.getDesignation(), h2FoundTextContent);
    }

    @Test
    @Order(23)
    void addNewShipWithoutCommanderShouldReturnErrorReturnOkWithCommanderWhileAddingItAgainShouldReturnError() throws IOException {
        HttpEntity<IdentityDto> shipHttpEntity =
                TestUtilities.createHttpEntity(withIdOfOne);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl + "/1/ship", shipHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        shipHttpEntity =
                TestUtilities.createHttpEntity(withIdOfOne);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl + "/1/ship", shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(24)
    void checkIfShowUpdateGunFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/ship/3/show-update-ship-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Choose Another Ship for " + fleetToBeAddedFirst.getDesignation(), h2FoundTextContent);
    }

    @Test
    @Order(25)
    void findShipsShouldReturnThePreviouslyAddedShip() {
        ResponseEntity<ShipDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1/ship", ShipDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 1);
        ShipDto shipDto = responseEntity.getBody()[0];
        assertEquals(1, shipDto.getId());
        assertEquals(1, shipDto.getShipClass().getId());
    }

    @Test
    @Order(26)
    void updateShipShouldReturnOk() throws IOException {
        HttpEntity<IdentityDto> shipHttpEntity =
                TestUtilities.createHttpEntity(withIdOfTwo);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1/ship/1", HttpMethod.PUT, shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(27)
    public void findShipInFleetByIdShouldReturnTheUpdatedShip() {
        ResponseEntity<ShipDto> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1/ship/2", ShipDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        ShipDto shipDto = responseEntity.getBody();
        assertEquals(2, shipDto.getId());
        assertEquals(2, shipDto.getShipClass().getId());
    }

    @Test
    @Order(28)
    void updateShipWithInvalidDataShouldReturnError() throws IOException {
        HttpEntity<ShipDto> shipHttpEntity =
                TestUtilities.createHttpEntity(shipInvalidData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1/ship/2", HttpMethod.PUT,shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(29)
    void postGunInstallationWithInvalidDataShouldReturnWithError() {
        HttpEntity<ShipDto> shipHttpEntity =
                TestUtilities.createHttpEntity(shipInvalidData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl + "/1/ship", shipHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(30)
    public void deleteShipFromFleetShouldReturnOkWhileDeletingNonexistentShouldReturnError() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1/ship/2", HttpMethod.DELETE,null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/1/ship/22", HttpMethod.DELETE,null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(31)
    public void deleteFleetShouldReturnOkDeletingTheSameAgainShouldReturnError() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE,null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.DELETE,null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }
}
