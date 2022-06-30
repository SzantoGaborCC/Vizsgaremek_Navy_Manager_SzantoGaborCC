package com.codecool.navymanager.integration;

import com.codecool.navymanager.dto.CountryDto;
import com.codecool.navymanager.dto.LoginData;
import com.codecool.navymanager.entity.Country;
import com.codecool.navymanager.response.JsonResponse;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CountryIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    private final String baseUrl = "/country";
    private final String loginURl = "/login";

    private final Country[] data = {
            new Country(1L, "Mexico"),
            new Country(2L, "Chile"),
            new Country(3L, "Argentina")
    };

    private static LoginData loginData = new LoginData("Admin", "security");

    private String addedMessage = "Country was added.";

    private CountryDto toBePosted = new CountryDto();

    {
        toBePosted.setName("Uruguay");
    }


 /*   @Test
    @Order(1)
    void tryToLoginWithRightCredentialsShouldReturnOk() {
        HttpEntity<LoginData> loginDataHttpEntity = TestUtilities.createHttpEntity(loginData);
        ResponseEntity<?> responseEntity
                = testRestTemplate.postForEntity(baseUrl + "/login", loginDataHttpEntity, CountryDto.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }*/

    @Test
    @Order(1)
    void getAllCountriesShouldReturnOkAndEmptyArray() {
        ResponseEntity<Country[]> responseEntity =
                testRestTemplate.getForEntity(baseUrl, Country[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        Assertions.assertTrue(responseEntity.getBody().length == 0);
    }

    @Test
    @Order(2)
    void postCountryShouldReturnOkAndTheExpectedDto() {
        HttpEntity<CountryDto> countryHttpEntity = TestUtilities.createHttpEntity(toBePosted);
        ResponseEntity<JsonResponse> responseEntity =
                testRestTemplate.postForEntity(baseUrl, countryHttpEntity, JsonResponse.class);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(addedMessage, responseEntity.getBody().getMessage());
    }

  /*  @Test
    @Order(3)
    void getWarehouseByIdTest() {
        ResponseEntity<Warehouse> responseEntity = testRestTemplate.getForEntity(warehouseBaseUrl + "/id/4", Warehouse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    @Order(4)
    void getWarehouseByNameTest() {
        ResponseEntity<Warehouse> responseEntity = testRestTemplate.getForEntity(warehouseBaseUrl + "/name/Test Name", Warehouse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    @Order(5)
    void getWarehouseByAddressTest() {
        ResponseEntity<Warehouse> responseEntity = testRestTemplate.getForEntity(warehouseBaseUrl + "/address/1234 Test City Test street 01", Warehouse.class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(expected, responseEntity.getBody());
    }

    @Test
    @Order(6)
    void getAllProductsInWarehouseTest() {
        Product[] expected = {
                new Product(1L, "Fifa", "Football videogame", ProductType.GAME, 22000,
                        ProductStatus.IN_STORAGE, null, null, null),
                new Product(2L, "Mario", "Jumping videogame", ProductType.GAME, 24999,
                        ProductStatus.IN_STORAGE, null, null, null),
                new Product(3L, "Doom", "Shooting videogame", ProductType.GAME, 5000,
                        ProductStatus.IN_STORAGE, null, null, null),
                new Product(4L, "Fifa", "Football videogame", ProductType.GAME, 22000,
                        ProductStatus.IN_STORAGE, null, null, null),
                new Product(5L, "Fifa", "Football videogame", ProductType.GAME, 22000,
                        ProductStatus.IN_STORAGE, null, null, null)
        };

        ResponseEntity<Product[]> responseEntity = testRestTemplate.getForEntity(warehouseBaseUrl + "/warehouse/1", Product[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertTrue(GenericArrayContentEqual.isEqual(expected, responseEntity.getBody()));
    }

    @Test
    @Order(7)
    void listWarehousesByNeededWorkersTest() {
        ResponseEntity<Warehouse[]> responseEntity = testRestTemplate.getForEntity(warehouseBaseUrl + "/workers_needed", Warehouse[].class);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        assertEquals(4L, responseEntity.getBody()[0].getId());
        assertEquals(3L, responseEntity.getBody()[1].getId());
        assertEquals(2L, responseEntity.getBody()[2].getId());
        assertEquals(1L, responseEntity.getBody()[3].getId());
    }

    @Test
    @Order(8)
    void updateWarehouseByIdTest() {
        WarehouseDTOWithoutId modifierDto = new WarehouseDTOWithoutId("Modified Name", "9999 Cityname Street Name street 99",
                9999, 100, 1500, 2000);

        testRestTemplate.put(warehouseBaseUrl + "/id/4", modifierDto);

        Warehouse after = testRestTemplate.getForEntity(warehouseBaseUrl + "/id/4", Warehouse.class).getBody();

        Warehouse expectedWarehouse = new Warehouse(modifierDto);
        expectedWarehouse.setId(4L);

        assertEquals(expectedWarehouse.getId(), after.getId());
        assertEquals(expectedWarehouse.getName(), after.getName());
        assertEquals(expectedWarehouse.getAddress(), after.getAddress());
        assertEquals(expectedWarehouse.getStorageSpace(), after.getStorageSpace());
        assertEquals(expectedWarehouse.getNumOfWorkers(), after.getNumOfWorkers());
        assertEquals(expectedWarehouse.getMaxWorkers(), after.getMaxWorkers());
        assertEquals(expectedWarehouse.getReqWorkers(), after.getReqWorkers());
    }

    @Test
    void deleteWarehouseByIdTest(){
        assertTrue(databaseContainsWarehouseWithId(4L));

        testRestTemplate.delete(warehouseBaseUrl + "/id/4");

        assertFalse(databaseContainsWarehouseWithId(4L));
    }

    private boolean databaseContainsWarehouseWithId(long id) {
        Warehouse[] warehouses = testRestTemplate.getForEntity(warehouseBaseUrl, Warehouse[].class).getBody();
        long num = Arrays.stream(warehouses).filter(w -> w.getId() == id).count();
        return num > 0;
    }

    private HttpEntity<WarehouseDTOWithoutId> createWarehouseDTOHttpEntity(WarehouseDTOWithoutId warehouseDTO) {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(warehouseDTO, httpHeaders);
    }*/
}
