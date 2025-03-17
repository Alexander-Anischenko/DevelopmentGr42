package de.ait.javalessons.controller;

import de.ait.javalessons.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

// Интеграционные тесты(IT)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RestApiCarControllerIT {

    // через аннотацию Autowired получаем помошника testRestTemplate, который помогает отправитьь предзапрос
    @Autowired
    private TestRestTemplate testRestTemplate;

    // подготовили BASE_URL для правильного пути
    private static final String BASE_URL = "/cars";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetCarsReturnDefaultCars(){
        //переменная, получившая  массив объектов Car через testRestTemplate.getForEntity прямо из приложения
        ResponseEntity<Car[]> response = testRestTemplate.getForEntity(BASE_URL, Car[].class);
        // проверяем, выдаст ли статус OK , значение статуса 200
        assertEquals(HttpStatus.OK, response.getStatusCode());
        //проверяем количество объектов
        assertEquals(4, response.getBody().length);
        // и название первого объекта
        assertEquals("BMW M1", response.getBody()[0].getName());
    }

    @Test
    void testGetCarByIdWasFound(){
        //переменная, получившая объект Car через testRestTemplate.getForEntity прямо из приложения
        ResponseEntity<Car> response = testRestTemplate.getForEntity(BASE_URL+"/1", Car.class);
        // проверяем, выдаст ли статус OK , значение статуса 200
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("BMW M1", response.getBody().getName()); // проверяем имя объекта
        assertEquals("1", response.getBody().getId()); // проверяем номер id
    }

    @Test
    void testGetCarByIdWasNotFound(){
        //переменная, получившая объект Car через testRestTemplate.getForEntity прямо из приложения
        ResponseEntity<Car> response = testRestTemplate.getForEntity(BASE_URL+"/10", Car.class);
        // проверяем, выдаст ли статус OK , значение статуса 200
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(null, response.getBody()); // проверяем, что response возвращает null
    }

    @Test
    void testPostCarAddNewCar(){
        Car carToAdd = new Car("5", "Tesla Model S");
        ResponseEntity<Car> response = testRestTemplate.postForEntity(BASE_URL, carToAdd, Car.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tesla Model S", response.getBody().getName());
        assertEquals("5", response.getBody().getId());
    }

    @Test
    void testCarUpdateCarInfo() {
        Car carToUpdate = new Car("5", "Tesla Model S");
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Car> request = new HttpEntity<>(carToUpdate, headers);
        ResponseEntity<Car> response = testRestTemplate.exchange(BASE_URL + "/1", HttpMethod.PUT, request, Car.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Tesla Model S", response.getBody().getName());
    }
}
