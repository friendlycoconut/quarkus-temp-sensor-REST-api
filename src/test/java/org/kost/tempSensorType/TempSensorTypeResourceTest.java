package org.kost.tempSensorType;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import io.quarkus.test.junit.mockito.InjectMock;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.mockito.Mockito;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;

@QuarkusTest
class TempSensorTypeResourceTest {

    @InjectMock
    TempSensorTypeService tempSensorTypeService;


    @Test
    void get() {

        List<TempSensorType> mockedList = new ArrayList<>();

        mockedList.add(new TempSensorType());
        mockedList.add(new TempSensorType());

        Mockito.when(tempSensorTypeService.findAll()).thenReturn(mockedList);

        given()

                .when().get("tempSensorTypes")
                .then()
                .statusCode(200)
                .body("size()", is(mockedList.size()));


    }

    @Test
    void saveFail() {

        TempSensorType createEntity = new TempSensorType();
        createEntity.setSensorTypeName("test");
        createEntity.setDescription("descr");
        createEntity.setRegulatingISO("test");

        given()
                .body(createEntity)
                .contentType("application/json")
                .when().post("manufacturers")
                .then()
                .statusCode(400);
    }

    @Test
    void saveSuccess() {

        TempSensorType createEntity = new TempSensorType();
        createEntity.setTempSensorTypeId(new AtomicInteger().getAndIncrement());
        createEntity.setSensorTypeName("test");
        createEntity.setDescription("descr");
        createEntity.setRegulatingISO("test");

        given()
                .body(createEntity)
                .contentType("application/json")
                .when().post("tempSensorTypes")
                .then()
                .statusCode(201);
    }

}