package de.ait.javalessons.controller;


import de.ait.javalessons.model.Car;
import de.ait.javalessons.repositories.CarRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

//для классов подобного рода принято в названии использовать слово Controller
@Slf4j // Аннотация Lombok для автоматического создания логгера
@RestController // Аннотация Spring, указывающая, что этот класс является REST-контроллером
// REST-контроллер — это класс, который обрабатывает HTTP-запросы и возвращает данные, как правило,
// в формате JSON или XML, то есть служит промежуточным звеном между клиентом (например, браузером или приложением) и сервером.
// Он помогает Spring понять, что этот класс нужно использовать для обработки запросов и возврата данных в удобном клиенту формате.
@RequestMapping("/cars") // Базовый путь для всех методов в этом контроллере
// Этот класс является первым слоем в многослойном приложении и встречает все запросы для дальнейшей обработки
public class RestApiCarController {
    // Список для хранения автомобилей
    private final CarRepository carRepository; // Репозиторий для работы с автомобилями
    // private final CarRepository carRepository; - Repository for working with cars

    // Конструктор класса, инициализирующий список автомобилей
    // Class constructor initializing the list of cars
    public RestApiCarController(CarRepository carRepository) {
        this.carRepository = carRepository;

        this.carRepository.saveAll(
                List.of(
                        new Car("1", "BMW M1"),
                        new Car("2", "Audi A8"),
                        new Car("3", "Kia Spartage"),
                        new Car("4", "Volvo 960")
                ));
    }

    /**
     * Метод для получения всех автомобилей.
     * GET-запрос на путь /cars
     * Iterable стоит на вершине коллекций и с ним можно только перебрать содержимое в цикле,
     * а также вернуть в том виде интерфейса с его методами, лежащего ниже по иерархии
     * который необходим в конкретном случае(List со всеми его методами, как пример)
     * @return список всех автомобилей
     */
    @GetMapping
    Iterable<Car> getCars() {
        return carRepository.findAll(); // Возвращает все сущьности из базы данных
    }

    /**
     * Метод для получения автомобиля по его ID.
     * GET-запрос на путь /cars/{id}
     *
     * @param id идентификатор автомобиля из URL-запроса (аннотация @PathVariable)
     * @return Optional, содержащий автомобиль, если он найден, иначе пустой Optional
     */
    @GetMapping("/{id}")
    /**
     *  Optional это специальный тип в Java, предназначенный для обозначения того, что значение может быть:
     *
     * присутствовать (Optional.of(car)), или
     * отсутствовать (Optional.empty()).
     * Это помогает избежать ошибки при попытке получения несуществующего элемента (например, при поиске автомобиля по ID, которого нет в списке).
     *То есть метод вернет либо автомобиль (Optional.of(car)), либо «пустоту» (Optional.empty()), если автомобиль не найден.
      */
    Optional<Car> getCarById(@PathVariable String id) { // @PathVariable показывает, что id берётся из адресной строки
//        for (Car car : carList) { //перебираем список автомобилей с помощью цикла forin
//            if (car.getId().equals(id)) { // обращаемся к геттеру класа Car и сравниваем идентификаторы
//                return Optional.of(car); // возвращаем автомобиль, если найден
//            }
//        }
        Optional<Car> carInDatabase = carRepository.findById(id);
        if (carInDatabase.isPresent()) {
            log.info("Car with id {} was found", id);
            return carInDatabase;
        }

        log.info("Car with id {} was not found", id);
        return Optional.empty();// возвращаем пустой Optional, если автомобиль не найден
    }

    /**
     * Метод для добавления нового автомобиля.
     * POST-запрос на /cars
     *
     * @param car объект автомобиля, переданный в теле запроса
     *            Аннотация @RequestBody указывает, что тело HTTP-запроса (JSON) преобразуется в Java-объект.
     * @return добавленный автомобиль
     */
    @PostMapping
    /*Аннотация @RequestBody в Spring используется в методах REST-контроллеров для того, чтобы указать, что данные,
    отправленные в теле HTTP-запроса, должны быть преобразованы в объект Java (в данном случае — объект класса Car).

Простыми словами:

Клиент отправляет запрос (например, POST-запрос) с информацией о новом автомобиле в формате JSON.
Аннотация @RequestBody говорит фреймворку Spring:
«Возьми данные из тела запроса и автоматически преврати их в объект класса Car».*/
    Car postCar(@RequestBody Car car) {//@RequestBody — это аннотация, которая сообщает Spring, что нужно преобразовать тело входящего HTTP-запроса в Java-объект.
//        carList.add(car);
//        return car;
        Car saveResult = carRepository.save(car);
        return saveResult;
    }

    /**
     * Метод для обновления существующего автомобиля по его ID.
     * PUT-запрос на /cars/{id}
     *
     * @param id  идентификатор автомобиля, который нужно обновить
     * @param car объект автомобиля с новыми данными
     * @return ResponseEntity содержит автомобиль и статус:
     *      *         - OK (200), если автомобиль найден и обновлён;
     *      *         - CREATED (201), если автомобиль не найден и был создан новый.
     */
    @PutMapping("/{id}")
    /*ResponseEntity<Car> — это объект, который используется для формирования ответа клиенту вместе с HTTP-статусом.
    Это позволяет вернуть данные вместе с информацией о результате выполнения операции
    (например, успешно ли прошёл запрос, или возникла ошибка).*/
    ResponseEntity<Car> putCar(@PathVariable String id, @RequestBody Car car) {
//        int index = -1; // переменная для хранения индекса найденного автомобиля
//        // Цикл для поиска автомобиля в списке по его идентификатору (id)
//        for (Car carToFind : carList) { // перебираем автомобили в списке
//            if (carToFind.getId().equals(id)) { // проверяем соответствие идентификаторов
//                index = carList.indexOf(carToFind); // сохраняем индекс найденного автомобиля
//                carList.set(index, car);// заменяем старую машину на новую (из тела запроса)
//            }
//        }
//
//        // Если index остался -1, значит машина с таким id не была найдена (используется тернарный оператор)
//        return (index == -1) ?
//                new ResponseEntity<>(postCar(car), HttpStatus.CREATED) :  // машина не найдена — создаём новую и возвращаем HTTP 201 (Created)
//                new ResponseEntity<>(car, HttpStatus.OK); // машина обновлена, возвращаем HTTP-статус 200 (OK)
        if(carRepository.existsById(id)){ // Если существует
            Car carToUpdate = carRepository.findById(id).get();// получить из базы данных
            carToUpdate.setName(car.getName()); // ставим новое имя, полученное из запроса
            carToUpdate.setId(id); // переписываем id
            Car savedCar = carRepository.save(carToUpdate); // сохраняем сущьность в репозитории
            return new ResponseEntity<>(savedCar, HttpStatus.OK); // Возвращаем сохраненную машину и HTTP-статус 200 (OK)
        }
        else { // машина не найдена — создаём новую и возвращаем HTTP 201 (Created)
            return new ResponseEntity<>(postCar(car), HttpStatus.CREATED);
        }
    }

    /**
     * Метод для удаления автомобиля по его ID.
     * DELETE-запрос на /cars/{id}
     *
     * @param id идентификатор автомобиля, который нужно удалить
     */
    @DeleteMapping("/{id}")
    void deleteCar(@PathVariable String id) {
        // Удаляем из списка все автомобили, чей идентификатор соответствует переданному значению id
        // Используется предикат (условие), проверяющий id каждого автомобиля на совпадение
        //Метод removeIf принимает предикат — условие, по которому элементы удаляются из коллекции.
        //Выражение car -> car.getId().equals(id) — это и есть предикат, который проверяет для каждого автомобиля в списке:
        //carList.removeIf(car -> car.getId().equals(id));
        carRepository.deleteById(id);
    }
}
