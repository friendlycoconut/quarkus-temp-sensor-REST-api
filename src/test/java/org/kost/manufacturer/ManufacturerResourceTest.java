package org.kost.manufacturer;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.Test;
import org.kost.tempSensorType.TempSensorType;
import org.kost.tempSensorType.TempSensorTypeService;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class ManufacturerResourceTest {

    @InjectMock
    ManufacturerService manufacturerService;


    @Test
    void get() {

        List<Manufacturer> mockedList = new ArrayList<>();

        mockedList.add(new Manufacturer());
        mockedList.add(new Manufacturer());
        mockedList.add(new Manufacturer());

        Mockito.when(manufacturerService.findAll()).thenReturn(mockedList);

        given()

                .when().get("manufacturers")
                .then()
                .statusCode(200)
                .body("size()", is(mockedList.size()));


    }

    @Test
    void postSuccess() {

        Manufacturer createEntity = new Manufacturer();
        createEntity.setManufacturerId(new AtomicInteger().getAndIncrement());
        createEntity.setManufacturerName("test");
        createEntity.setDescription("descr");
        createEntity.setEmail("email@email.com");

        given()
                .body(createEntity)
                .contentType("application/json")
                .when().post("manufacturers")
                .then()
                .statusCode(201);
    }


    @Test
    void postNotFoundCode() {

        Manufacturer createEntity = new Manufacturer();
        createEntity.setManufacturerId(new AtomicInteger().getAndIncrement());
        createEntity.setManufacturerName("test");
        createEntity.setDescription("descr");
        createEntity.setEmail("email@email.com");

        given()
                .body(createEntity)
                .contentType("application/json")
                .when().post()
                .then()
                .statusCode(404);
    }


}