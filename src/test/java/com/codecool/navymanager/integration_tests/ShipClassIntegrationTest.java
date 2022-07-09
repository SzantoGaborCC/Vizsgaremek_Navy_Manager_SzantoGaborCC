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
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
class ShipClassIntegrationTest {
    @Autowired
    private ServletWebServerApplicationContext webServerAppCtxt;

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final WebClient webClient = new WebClient();

    {
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(false);
    }

    private final String baseUrl = "/ship-class";
    private final String baseUrlAPI = "/ship-class/api";

    private final String gunUrl = "/gun/api";
    private final String countryUrl = "/country/api";

    private final String hullClassificationUrl = "/hull-classification/api";

    private final CountryDto country1 = CountryDto.builder().id(1L).name("Berdola").build();
    private final CountryDto country2 = CountryDto.builder().id(2L).name("Tekkele").build();

    private final HullClassificationDto hullClassification1 =
            HullClassificationDto.builder().id(1L).abbreviation("CC").designation("Circular Cruiser").build();
    private final HullClassificationDto hullClassification2 =
            HullClassificationDto.builder().id(2L).abbreviation("FF").designation("Feeble Frigate").build();

   private final GunDto gun1 = GunDto.builder()
                    .id(1L)
                    .designation("Big gun")
                    .caliberInMms(380)
                    .projectileWeightInKgs(750)
                    .rangeInMeters(30000)
                    .country(country1).build();

    private final GunDto gun1withCountry2 = GunDto.builder()
            .id(2L)
            .designation("Big gun")
            .caliberInMms(380)
            .projectileWeightInKgs(750)
            .rangeInMeters(30000)
            .country(country2).build();

    private final GunDto gun2 = GunDto.builder()
            .id(3L)
            .designation("Medium gun")
            .caliberInMms(150)
            .projectileWeightInKgs(45)
            .rangeInMeters(20000)
            .country(country2).build();

    private final GunDto gun2WithCountry1 = GunDto.builder()
            .id(4L)
            .designation("Medium gun")
            .caliberInMms(150)
            .projectileWeightInKgs(45)
            .rangeInMeters(20000)
            .country(country1).build();

    private final ShipClassDto[] data = {
            ShipClassDto.builder()
                    .name("Alaska")
                    .displacementInTons(33333)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(33)
                    .armorDeckInCms(12)
                    .armorTurretInCms(35)
                    .speedInKmh(50)
                    .country(country1).build(),
            ShipClassDto.builder()
                    .name("Beefy")
                    .displacementInTons(31333)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(37)
                    .armorDeckInCms(13)
                    .armorTurretInCms(31)
                    .speedInKmh(51)
                    .country(country1).build(),
            ShipClassDto.builder()
                    .name("Perkele")
                    .displacementInTons(22222)
                    .hullClassification(hullClassification2)
                    .armorBeltInCms(22)
                    .armorDeckInCms(11)
                    .armorTurretInCms(33)
                    .speedInKmh(55)
                    .country(country2).build(),
            ShipClassDto.builder()
                    .name("Certef")
                    .displacementInTons(11111)
                    .hullClassification(hullClassification2)
                    .armorBeltInCms(11)
                    .armorDeckInCms(2)
                    .armorTurretInCms(11)
                    .speedInKmh(60)
                    .country(country2).build(),
    };



    private final ShipClassDto shipClassToBeAddedFirst =
            ShipClassDto.builder()
                    .name("Certef")
                    .displacementInTons(11111)
                    .hullClassification(hullClassification2)
                    .armorBeltInCms(11)
                    .armorDeckInCms(2)
                    .armorTurretInCms(11)
                    .speedInKmh(60)
                    .country(country2).build();

    private final ShipClassDto shipClassToBeAddedFirstWithIdToTriggerError =
            ShipClassDto.builder()
                    .id(1L)
                    .name("Certef")
                    .displacementInTons(11111)
                    .hullClassification(hullClassification2)
                    .armorBeltInCms(11)
                    .armorDeckInCms(2)
                    .armorTurretInCms(11)
                    .speedInKmh(60)
                    .country(country2).build();
    private final ShipClassDto shipClassForUpdatedNoIdLeadsToError =
            ShipClassDto.builder()
                    .name("Gargantuan")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22)
                    .country(country1).build();
    private final ShipClassDto shipClassToBeUpdatedInvalidIdLeadsToError =
            ShipClassDto.builder()
                    .id(22L)
                    .name("Gargantuan")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22)
                    .country(country1).build();
    private final ShipClassDto shipClassWithUpdateDataValid =
            ShipClassDto.builder()
                    .id(2L)
                    .name("Gargantuan")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22)
                    .country(country1).build();
    private final ShipClassDto shipClassNullName =
            ShipClassDto.builder()
                    .id(2L)
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22)
                    .country(country1).build();

    private final ShipClassDto shipClassEmptyName =
            ShipClassDto.builder()
                    .id(2L)
                    .name("")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22)
                    .country(country1).build();


    private final ShipClassDto shipClassForAdditionMissingNumericalData =
            ShipClassDto.builder()
                    .name("Gargantuan")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .country(country1).build();

    private final ShipClassDto shipClassForUpdateMissingNumericalData =
            ShipClassDto.builder()
                    .id(2L)
                    .name("Gargantuan")
                    .country(country1).build();

    private final ShipClassDto shipClassForAdditionNameAndCountryDuplicated =
            ShipClassDto.builder()
                    .name("Certef")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22)
                    .country(country2).build();

    private final ShipClassDto shipClassForUpdateNameAndCountryDuplicated =
            ShipClassDto.builder()
                    .id(3L)
                    .name("Certef")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22)
                    .country(country2).build();

    private final ShipClassDto shipClassForAdditionMissingCountry =
            ShipClassDto.builder()
                    .name("Gargantuan")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22).build();

    private final ShipClassDto shipClassForUpdateMissingCountry =
            ShipClassDto.builder()
                    .id(2L)
                    .name("Gargantuan")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22).build();

    private final ShipClassDto shipClassWithUpdateData =
            ShipClassDto.builder()
                    .id(3L)
                    .name("Gargantuan22")
                    .displacementInTons(111111)
                    .hullClassification(hullClassification1)
                    .armorBeltInCms(111)
                    .armorDeckInCms(22)
                    .armorTurretInCms(111)
                    .speedInKmh(22)
                    .country(country1).build();

    private final GunInstallationDto gunInstallation1 = GunInstallationDto.builder()
            .gun(gun2)
            .quantity(12)
            .build();

    private final GunInstallationDto gunInstallation2 = GunInstallationDto.builder()
            .gun(gun1)
            .quantity(8)
            .build();

    private final GunInstallationDto gunInstallation1WithCountry2 = GunInstallationDto.builder()
            .gun(gun1withCountry2)
            .quantity(12)
            .build();


    private final GunInstallationDto gunInstallation1b = GunInstallationDto.builder()
            .gun(gun1)
            .quantity(12)
            .build();

    private final GunInstallationDto gunInstallation1ForUpdateWithCountry2NoData = GunInstallationDto.builder().build();

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

        HttpEntity<GunDto> gunHttpEntity =
                Utils.createHttpEntity(gun1);
        responseEntity =
                testRestTemplate.postForEntity(gunUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        gunHttpEntity =
                Utils.createHttpEntity(gun1withCountry2);
        responseEntity =
                testRestTemplate.postForEntity(gunUrl, gunHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        gunHttpEntity =
                Utils.createHttpEntity(gun2);
        responseEntity =
                testRestTemplate.postForEntity(gunUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        gunHttpEntity =
                Utils.createHttpEntity(gun2WithCountry1);
        responseEntity =
                testRestTemplate.postForEntity(gunUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    void getAllShipClassesShouldReturnOkAndEmptyArray() {
        ResponseEntity<ShipClassDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrlAPI, ShipClassDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(3)
    void postShipClassShouldReturnOkDuplicateShouldReturnWithError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassToBeAddedFirst);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        shipClassHttpEntity = Utils.createHttpEntity(shipClassToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    void getTheAddedShipClassShouldHaveExpectedData() {
        ResponseEntity<ShipClassDto> responseEntity =
                testRestTemplate.getForEntity(baseUrlAPI + "/1", ShipClassDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1L, responseEntity.getBody().getId());
        assertEquals("Certef", responseEntity.getBody().getName());
        assertEquals(11111, responseEntity.getBody().getDisplacementInTons());
        assertEquals(11, responseEntity.getBody().getArmorBeltInCms());
        assertEquals(2, responseEntity.getBody().getArmorDeckInCms());
        assertEquals(11, responseEntity.getBody().getArmorTurretInCms());
        assertEquals(60, responseEntity.getBody().getSpeedInKmh());
        assertEquals(2, responseEntity.getBody().getCountry().getId());
    }

    @Test
    @Order(5)
    void postMultipleShipClassesThenCheckTheReturnedArrayUsingShipClassNames() {
        HttpEntity<ShipClassDto> shipClassHttpEntity;
        for (ShipClassDto shipClassDto : data) {
            if (shipClassDto.getName().equals("Certef"))
                continue;
            shipClassHttpEntity = Utils.createHttpEntity(shipClassDto);
            testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        }
        ResponseEntity<ShipClassDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrlAPI, ShipClassDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        List<String> dataAsListJustNames = Arrays.stream(data).map(ShipClassDto::getName).toList();
        List<String> returnedListJustNames =
                Arrays.stream(responseEntity.getBody()).map(ShipClassDto::getName).toList();
        assertTrue(dataAsListJustNames.containsAll(returnedListJustNames));
    }

    @Test
    @Order(6)
    void updateShipClassWithNullIdShouldReturnIdError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/2", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateShipClassWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void updateShipClassIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/22", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void addShipClassIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        shipClassHttpEntity = Utils.createHttpEntity(shipClassEmptyName);
        responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void updateShipClassIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/2", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        shipClassHttpEntity = Utils.createHttpEntity(shipClassEmptyName);
        responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/2", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void addShipClassIfNameAndCountryIsDuplicatedShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassForAdditionNameAndCountryDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(12)
    void updateShipClassIfNameAndCountryIsDuplicatedShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassForUpdateNameAndCountryDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/3", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        ResponseEntity<ShipClassDto[]> responseEntityForList =
                testRestTemplate.getForEntity(baseUrlAPI, ShipClassDto[].class);
        assertEquals(HttpStatus.OK, responseEntityForList.getStatusCode());
        System.out.println(Arrays.toString(responseEntityForList.getBody()));
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(13)
    void deleteShipClassIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeBadRequest() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(14)
    void addShipClassIfNumericalDataIsMissingShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassForAdditionMissingNumericalData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(15)
    void updateShipClassIfNumericalDataIsMissingShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassForUpdateMissingNumericalData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(16)
    void addShipClassIfCountryIsMissingShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassForAdditionMissingCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(17)
    void updateShipClassIfCountryIsMissingShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassForUpdateMissingCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/4", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
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
        assertEquals("List of Ship Classes", h2FoundTextContent);
    }

    @Test
    @Order(18)
    void checkIfShowAddFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/show-add-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Add New Ship Class", h2FoundTextContent);
    }

    @Test
    @Order(19)
    void checkIfShowDetailsPageIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-details-page");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals(shipClassToBeAddedFirst.getName() + " Class - Details", h2FoundTextContent);
    }

    @Test
    @Order(20)
    void checkIfShowUpdateFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/show-update-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Update Ship Class", h2FoundTextContent);
    }

    @Test
    @Order(21)
    void updateShipClassWithValidDataShouldReturnOk() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                Utils.createHttpEntity(shipClassWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/3", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(22)
    void checkIfShowAddGunFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/gun/show-add-gun-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Add New Gun to " + shipClassToBeAddedFirst.getName() + " Class" , h2FoundTextContent);
    }

    @Test
    @Order(23)
    void addNewGunInstallationShouldReturnOkAddingItAgainShouldReturnError() throws IOException {
        HttpEntity<GunInstallationDto> gunInstallationHttpEntity =
                Utils.createHttpEntity(gunInstallation1);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI + "/1/gun", gunInstallationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        gunInstallationHttpEntity =
                Utils.createHttpEntity(gunInstallation1);
        responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI + "/1/gun", gunInstallationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(24)
    void checkIfShowUpdateGunFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/gun/3/show-update-gun-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Update Gun for " + shipClassToBeAddedFirst.getName() + " Class" , h2FoundTextContent);
    }

    @Test
    @Order(25)
    void findGunsShouldReturnThePreviouslyAddedGunInstallation() {
        ResponseEntity<GunInstallationDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrlAPI + "/1/gun", GunInstallationDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 1);
        GunInstallationDto gunInstallationDto = responseEntity.getBody()[0];
        assertEquals(3, gunInstallationDto.getGun().getId());
        assertEquals(12, gunInstallationDto.getQuantity());
    }

    @Test
    @Order(26)
    void updateGunInstallationShouldReturnOk() throws IOException {
        HttpEntity<GunInstallationDto> gunInstallationHttpEntity =
                Utils.createHttpEntity(gunInstallation1WithCountry2);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1/gun/3", HttpMethod.PUT,gunInstallationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(27)
    public void findGunInShipClassByIdShouldReturnTheUpdatedGunInstallation() {
        ResponseEntity<GunInstallationDto> responseEntity =
                testRestTemplate.getForEntity(baseUrlAPI + "/1/gun/2", GunInstallationDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        GunInstallationDto gunInstallationDto = responseEntity.getBody();
        assertEquals(2, gunInstallationDto.getGun().getId());
        assertEquals(12, gunInstallationDto.getQuantity());
    }

    @Test
    @Order(28)
    void updateGunInstallationWithInvalidDataShouldReturnError() throws IOException {
        HttpEntity<GunInstallationDto> gunInstallationHttpEntity =
                Utils.createHttpEntity(gunInstallation1ForUpdateWithCountry2NoData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1/gun/3", HttpMethod.PUT,gunInstallationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(28)
    void updateGunInstallationWithWrongCountryShouldReturnError() throws IOException {
        HttpEntity<GunInstallationDto> gunInstallationHttpEntity =
                Utils.createHttpEntity(gunInstallation1b);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1/gun/2", HttpMethod.PUT,gunInstallationHttpEntity, JsonResponse.class);
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(29)
    void postGunInstallationWithInvalidDataShouldReturnWithError() {
        HttpEntity<GunInstallationDto> gunInstallationHttpEntity =
                Utils.createHttpEntity(gunInstallation1ForUpdateWithCountry2NoData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI + "/1/gun", gunInstallationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(30)
    void addNewGunInstallationWrongCountryShouldReturnError() throws IOException {
        HttpEntity<GunInstallationDto> gunInstallationHttpEntity =
                Utils.createHttpEntity(gunInstallation2);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrlAPI + "/1/gun", gunInstallationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(31)
    public void deleteGunFromShipClassShouldReturnOkDeletingTheSameAgainShouldReturnError() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1/gun/2", HttpMethod.DELETE,null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1/gun/2", HttpMethod.DELETE,null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(32)
    public void deleteShipClassShouldReturnOkDeletingTheSameAgainShouldReturnError() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1", HttpMethod.DELETE,null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrlAPI + "/1", HttpMethod.DELETE,null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }
}
