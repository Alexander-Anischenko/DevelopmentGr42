package de.ait.javalessons.controller;

import de.ait.javalessons.model.Car;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RestApiCarControllerTest {
    private RestApiCarController restApiCarController;

    // Инициализация контроллера перед каждым тестом с набором стандартных автомобилей
    @BeforeEach
    public void setup() {
        restApiCarController = new RestApiCarController(null);
    }

    // Проверка, что метод getCars возвращает список автомобилей по умолчанию
    @Test
    void testGetCarsReturnDefaultCars() {
        // Получаем все автомобили с помощью метода getCars(), который возвращает Iterable<Car>
        Iterable<Car> resultCarsIterable = restApiCarController.getCars();
        // Создаем переменную ArrayList
        List<Car> resultCars = new ArrayList<>();
        // Конвертируем Iterable в ArrayList перебирая resultCarsIterable с помощью метода forEach
        // и добавляя с помощью метода add в переменную resultCars
        resultCarsIterable.forEach(resultCars::add);

        // Проверяем количество и корректность первого элемента
        assertEquals(4, resultCars.size());
        assertEquals("BMW M1", resultCars.get(0).getName());
    }

    // Проверка успешного нахождения автомобиля по существующему идентификатору
    @Test
    void testGetCarByIdWasFound(){
        Optional<Car> result = restApiCarController.getCarById("1");
        // Проверка на наличие результата с помощью метода isPresent, возвращающего буленовское выражение
        assertTrue(result.isPresent());
        // Проверка на ожидаемый Объект с помощю метода get, возвращающего оъект Car и метода геттера класса Car
        assertEquals("BMW M1", result.get().getName());
    }

    // Проверка, что метод возвращает пустой результат, если автомобиль с ID не найден
    @Test
    void testGetCarByIdWasNotFound(){
        Optional<Car> result = restApiCarController.getCarById("10");
        // Проверка на наличие результата с помощью метода isPresent, возвращающего буленовское выражение
        assertFalse(result.isPresent());

    }

    // Тест на добавление нового автомобиля
    @Test
    void testPostCarAddNewCar(){
        // создаем новый объект
        Car carToAdd = new Car("5", "Tesla Model 1");
        // добавляем новый автомобиль в список автомобилей с помощью метода postCar
        Car result = restApiCarController.postCar(carToAdd);
        //проверяем наличие автомобиля по имени
        assertEquals("Tesla Model 1", result.getName());
        // и по id
        assertEquals("5", result.getId());

        Iterable<Car> resultCarsIterable = restApiCarController.getCars();
        // Создаем переменную ArrayList
        List<Car> resultCars = new ArrayList<>();
        // Переводим Iterable в ArrayList перебирая resultCarsIterable с помощью метода forEach
        // и добавляя с помощью метода add в переменную resultCars
        resultCarsIterable.forEach(resultCars::add);
        // проверяем на соответствующее количество автомобилей
        assertEquals(5, resultCars.size());
    }

    // Проверка, что существующий автомобиль корректно обновляется по ID
    @Test
    void testPutCarUpdateCar(){
        // создаем новый объект
        Car carToUpdate = new Car("1", "Tesla Model 1");
        //меняем данные объекта с id 1 на новые с помощью метода putCar
        ResponseEntity<Car> responseEntityResult  = restApiCarController.putCar("1", carToUpdate);
        //проверяем значение HTTP статуса
        assertEquals(200, responseEntityResult.getStatusCode().value());
        // проверяем изменилось ли название автомобиля с помощью метода getBody(возвращает Car) класса ResponseEntity и геттера класса Car
        assertEquals("Tesla Model 1", responseEntityResult.getBody().getName());
    }

    @Test
    void testPutCarCreateNewCar(){
        // создаем новый объект
        Car carToUpdate = new Car("10", "Tesla Model 1");
        //меняем данные объекта с id 1 на новые с помощью метода putCar
        ResponseEntity<Car> responseEntityResult  = restApiCarController.putCar("10", carToUpdate);
        //проверяем значение HTTP статуса
        assertEquals(201, responseEntityResult.getStatusCode().value());
        // проверяем изменилось ли название автомобиля с помощью метода getBody(возвращает Car) класса ResponseEntity и геттера класса Car
        assertEquals("Tesla Model 1", responseEntityResult.getBody().getName());
    }

    // Проверка, что метод удаления удаляет автомобиль по ID корректно
    @Test
    void testDeleteCarSuccess(){
        // Получаем список автомобилей до удаления
        Iterable<Car> resultCarsIterable = restApiCarController.getCars();
        // Создаем переменную ArrayList
        List<Car> resultCars = new ArrayList<>();
        // Переводим Iterable в ArrayList перебирая resultCarsIterable с помощью метода forEach
        // и добавляя с помощью метода add в переменную resultCars
        resultCarsIterable.forEach(resultCars::add);
        // проверяем на соответствующее количество автомобилей
        assertEquals(4, resultCars.size());
        // удаляем автомобиль с id 1
        restApiCarController.deleteCar("1");
        // Получаем список автомобилей после удаления
        Iterable<Car> resultCarsIterableDeletedCars = restApiCarController.getCars();
        // Очищаем и заново конвертируем список после удаления
        resultCars = new ArrayList<>();
        resultCarsIterableDeletedCars.forEach(resultCars::add);
        //проверяем количество автомобилей после удаления
        assertEquals(3, resultCars.size());
    }
}
