package de.ait.javalessons.controller;

import de.ait.javalessons.model.Movie;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

//для классов подобного рода принято в названии использовать слово Controller
@Slf4j // Аннотация Lombok для автоматического создания логгера
@RestController // Аннотация Spring, указывающая, что этот класс является REST-контроллером
// REST-контроллер — это класс, который обрабатывает HTTP-запросы и возвращает данные, как правило,
// в формате JSON или XML, то есть служит промежуточным звеном между клиентом (например, браузером или приложением) и сервером.
// Он помогает Spring понять, что этот класс нужно использовать для обработки запросов и возврата данных в удобном клиенту формате.
@RequestMapping("/cars") // Базовый путь для всех методов в этом контроллере
public class MovieController {

    private List<Movie> movies = new ArrayList<>();

    public MovieController() {
        movies.addAll(List.of(new Movie(1, "Alien", "Sci-fi", 1979),
                new Movie(2, "The Thing", "Horror", 1982),
                new Movie(3, "Terminator", "Sci-fi", 1984),
                new Movie(4, "Mad Max", "Action", 1979)));
    }

    // Возвращает список всех фильмов.
    // Returns a list of all movies.
    @GetMapping
    public List<Movie> getMovies() {
        return movies;
    }

    // Возвращает конкретный фильм по его id. Если фильм не найден,
    // верните сообщение об ошибке или подходящий HTTP-статус (например, 404 Not Found).
    // Returns a specific movie by its id. If the movie is not found,
    // return an error message or an appropriate HTTP status (e.g., 404 Not Found)
    @GetMapping("/{id}")
    public ResponseEntity<Movie> getMovieById(@PathVariable int id) { // @PathVariable показывает, что id берётся из адресной строки
        return movies.stream() //вызываем поток
                .filter(movie -> movie.getId() == id) // фильтруем по полученному id
                .findFirst() // возвращает Optional тоесть или первый найденный, или empty
                .map(ResponseEntity::ok) // если фильм найден, метод map преобразует его в ResponseEntity с кодом 200 OK
                // если фильм не найден, метод orElseGet возвращает ResponseEntity со статусом 404 NOT_FOUND и телом, равным null
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(null));

    }

    // Добавляет новый фильм в список. Данные о фильме могут приходить в формате JSON.
    // Верните в ответе информацию о добавленном фильме или статус 201 Created.
    // Adds a new movie to the list. Movie data can come in JSON format.
    // Return information about the added movie or status 201 Created in the response
    @PostMapping
    public ResponseEntity<Movie> addMovie(@RequestBody Movie movie) {//@RequestBody — это аннотация, которая сообщает Spring, что нужно преобразовать тело входящего HTTP-запроса в Java-объект.
        movies.add(movie); //добавляем вернувшийся из запроса объект в конец List
        // Возвращаем ResponseEntity с HTTP-статусом 201 CREATED, включающим объект movie в теле ответа.
        return ResponseEntity.status(HttpStatus.CREATED).body(movie);
    }

    // Удаляет фильм из списка по id.
    // Deletes a movie from the list by id.
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleetMovie(@PathVariable int id) {  // @PathVariable показывает, что id берётся из адресной строки
        // Возвращает true, если хотя бы один элемент был удалён, и false – если ни один элемент не подошёл под условие
        boolean result = movies.removeIf(movie -> movie.getId() == id); //удаляем фильм при условии совпадения по переменной id
        if (result) { // если result true возвращаем ResponseEntity с кодом 200 OK и сообщением об успешном удалении
            return ResponseEntity.ok("Movie with id " + id + " was deleted");
        } else { // Если result false возвращаем ResponseEntity с кодом 404 NOT_FOUND и сообщением об отсутствии фильма
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Movie with id " + id + " was not found");
        }

    }

}
