package de.ait.javalessons.controller;

import de.ait.javalessons.model.Movie;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MovieControllerTest {
    private MovieController restApiMovieController;

    @BeforeEach
    void setUp() {
        restApiMovieController = new MovieController();
    }

    @Test
    void getMoviesTestDefaultMovies() {
        Iterable<Movie> iterableMovies = restApiMovieController.getMovies();
        List<Movie> moviesList = new ArrayList<>();
        iterableMovies.forEach(moviesList::add);

        assertEquals(4, moviesList.size());

        assertEquals("Alien", moviesList.get(0).getTitle());
    }
    @Test
    void getMovieByIdTestCarWasFound() {
        ResponseEntity<Movie> result = restApiMovieController.getMovieById(1);

        assertTrue(result.getBody() != null);

        assertEquals("Alien", result.getBody().getTitle());
    }

    @Test
    void getMovieByIdTestCarWasNotFound() {
        ResponseEntity<Movie> result = restApiMovieController.getMovieById(0);

        assertFalse(result.getBody() != null);
    }

    @Test
    void addMovieTest() {
        Movie movieToAdd = new Movie(5, "Matrix", "Sci-fi", 1999);
        restApiMovieController.addMovie(movieToAdd);

        Iterable<Movie> movieIterable = restApiMovieController.getMovies();
        List<Movie> movieList = new ArrayList<>();
        movieIterable.forEach(movieList::add);

        assertEquals(5, movieList.size());
        assertEquals(movieToAdd.getId(), movieList.get(4).getId());
        assertEquals(movieToAdd.getTitle(), movieList.get(4).getTitle());
    }

    @Test
    void deleteMovieByIdTestExistingMovie() {
        ResponseEntity<String> response = restApiMovieController.deleetMovie(1);
        assertEquals("Movie with id 1 was deleted", response.getBody());

        Iterable<Movie> movieIterable = restApiMovieController.getMovies();
        List<Movie> movieList = new ArrayList<>();
        movieIterable.forEach(movieList::add);

        assertEquals(3, movieList.size());
    }

    @Test
    void deleteMovieByIdTestNotExistingMovie() {
        ResponseEntity<String> response = restApiMovieController.deleetMovie(0);
        assertEquals("Movie with id 0 was not found", response.getBody());

        Iterable<Movie> movieIterable = restApiMovieController.getMovies();
        List<Movie> movieList = new ArrayList<>();
        movieIterable.forEach(movieList::add);

        assertEquals(4, movieList.size());
    }
}
