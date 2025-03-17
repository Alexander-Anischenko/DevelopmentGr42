package de.ait.javalessons.controller;

import de.ait.javalessons.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MovieControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;


    private static final String BASE_URL = "/movies";

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetMovieReturnDefaultMovies() {
        ResponseEntity<Movie[]>response = restTemplate.getForEntity(BASE_URL, Movie[].class);
        assertEquals(HttpStatus.OK,response.getStatusCode());
        assertEquals(4,response.getBody().length);
        assertEquals("Alien",response.getBody()[0].getTitle());
    }

    @Test
    void testGetAllMovies() {
        ResponseEntity<Movie[]> response = restTemplate.getForEntity("/movies", Movie[].class);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody().length);
        assertEquals(4, response.getBody().length);
    }

    @Test
    void testGetMovieById() {
        ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/3", Movie.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(3L, response.getBody().getId());
        assertEquals("Terminator", response.getBody().getTitle());
    }

    @Test
    void testGetMovieByInvalidId() {
        ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/10", Movie.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    @Test
    void testAddMovieWithCorrectId() {
        Movie movie = new Movie(6, "Matrix", "Sci-fi", 1999);
        ResponseEntity<Movie> response = restTemplate.postForEntity("/movies", movie, Movie.class);
        assertEquals(201, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(6L, response.getBody().getId());
    }

    @Test
    void testAddMovieWithExistingId() {
        Movie newMovie = new Movie(1, "Interstellar", "Sci-Fi", 2014);
        ResponseEntity<Movie> response = restTemplate.postForEntity("/movies", newMovie, Movie.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().getId());
    }

    @Test
    void testAddMovieWithIncorrectId() {
        Movie newMovie = new Movie(10, "The Matrix", "Action", 1999);
        ResponseEntity<Movie> response = restTemplate.postForEntity("/movies", newMovie, Movie.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(10, response.getBody().getId());
    }

    @Test
    void testDeleteMovieFailed() {
        restTemplate.delete("/movies/2");

        ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/20", Movie.class);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testDeleteMovieSuccess() {
        restTemplate.delete("/movies/2L");

        ResponseEntity<Movie> response = restTemplate.getForEntity("/movies/2", Movie.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

    }

}
