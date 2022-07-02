package com.codecool.navymanager.integration;

import com.codecool.navymanager.TestUtilities;
import com.codecool.navymanager.dto.*;
import com.codecool.navymanager.entity.HullClassification;
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
import java.util.Arrays;
import java.util.List;
import java.util.Set;

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
    private final String gunUrl = "/gun";
    private final String countryUrl = "/country";

    private final String hullClassificationUrl = "/hull-classification";

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
    private final GunDto gun2 = GunDto.builder()
                    .id(2L)
                    .designation("Medium gun")
                    .caliberInMms(150)
                    .projectileWeightInKgs(45)
                    .rangeInMeters(20000)
                    .country(country2).build();

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

        HttpEntity<GunDto> gunHttpEntity =
                TestUtilities.createHttpEntity(gun1);
        responseEntity =
                testRestTemplate.postForEntity(gunUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        gunHttpEntity =
                TestUtilities.createHttpEntity(gun2);
        responseEntity =
                testRestTemplate.postForEntity(gunUrl, gunHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(2)
    void getAllShipClassesShouldReturnOkAndEmptyArray() {
        ResponseEntity<ShipClassDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, ShipClassDto[].class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(3)
    void postShipClassShouldReturnOkDuplicateShouldReturnWithError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassToBeAddedFirst);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());

        shipClassHttpEntity = TestUtilities.createHttpEntity(shipClassToBeAddedFirstWithIdToTriggerError);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(4)
    void getTheAddedShipClassShouldHaveExpectedData() {
        ResponseEntity<ShipClassDto> responseEntity =
                testRestTemplate.getForEntity(baseUrl + "/1", ShipClassDto.class);
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
            shipClassHttpEntity = TestUtilities.createHttpEntity(shipClassDto);
            testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        }
        ResponseEntity<ShipClassDto[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, ShipClassDto[].class);
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
                TestUtilities.createHttpEntity(shipClassForUpdatedNoIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/2", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(7)
    void updateShipClassWithIdDifferentThanPathVariableShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassWithUpdateDataValid);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/1", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(8)
    void updateShipClassIfNotAlreadyExistsShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassToBeUpdatedInvalidIdLeadsToError);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/22", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(9)
    void addShipClassIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        shipClassHttpEntity = TestUtilities.createHttpEntity(shipClassEmptyName);
        responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(10)
    void updateShipClassIfDesignationIsNullOrLengthIsZeroShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassNullName);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/2", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        shipClassHttpEntity = TestUtilities.createHttpEntity(shipClassEmptyName);
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/2", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(11)
    void addShipClassIfNameAndCountryIsDuplicatedShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassForAdditionNameAndCountryDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(12)
    void updateShipClassIfNameAndCountryIsDuplicatedShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassForUpdateNameAndCountryDuplicated);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/3", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        ResponseEntity<ShipClassDto[]> responseEntityForList =
                testRestTemplate.getForEntity(baseUrl, ShipClassDto[].class);
        assertEquals(HttpStatus.OK, responseEntityForList.getStatusCode());
        System.out.println(Arrays.toString(responseEntityForList.getBody()));
        System.out.println(responseEntity.getBody().getErrorDescription());
        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
    }

    @Test
    @Order(13)
    void deleteShipClassIfExistsShouldReturnOkIfItDoesNotExistThenShouldBeBadRequest() {
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.DELETE, null, JsonResponse.class);
        assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, responseEntity.getStatusCode());
    }

    @Test
    @Order(14)
    void addShipClassIfNumericalDataIsMissingShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassForAdditionMissingNumericalData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(15)
    void updateShipClassIfNumericalDataIsMissingShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassForUpdateMissingNumericalData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(16)
    void addShipClassIfCountryIsMissingShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassForAdditionMissingCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, shipClassHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
    }

    @Test
    @Order(17)
    void updateShipClassIfCountryIsMissingShouldReturnError() {
        HttpEntity<ShipClassDto> shipClassHttpEntity =
                TestUtilities.createHttpEntity(shipClassForUpdateMissingCountry);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/4", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
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
                TestUtilities.createHttpEntity(shipClassWithUpdateData);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.exchange(baseUrl + "/3", HttpMethod.PUT, shipClassHttpEntity, JsonResponse.class);
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
    void addNewGunInstallationShouldReturnOk() throws IOException {
        GunInstallationDto gunInstallation = GunInstallationDto.builder()
                .gun(gun2)
                .quantity(12)
                .build();
        HttpEntity<GunInstallationDto> gunInstallationHttpEntity =
                TestUtilities.createHttpEntity(gunInstallation);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl + "/1/gun", gunInstallationHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }

    @Test
    @Order(24)
    void checkIfShowUpdateGunFormIsWorking() throws IOException {
        HtmlPage htmlPage = webClient.getPage("http://localhost:" +
                webServerAppCtxt.getWebServer().getPort() + baseUrl + "/1/gun/2/show-update-gun-form");
        DomNodeList<DomElement> h2headers = htmlPage.getElementsByTagName("h2");
        assertThat(h2headers).hasSize(1);
        String h2FoundTextContent = h2headers.get(0).getTextContent();
        assertEquals("Update Gun for " + shipClassToBeAddedFirst.getName() + " Class" , h2FoundTextContent);
    }
}
