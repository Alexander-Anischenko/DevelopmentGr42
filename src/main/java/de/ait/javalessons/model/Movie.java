package de.ait.javalessons.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

// Аннотация, заменяющая конструктор, геттеры и сеттеры
@AllArgsConstructor
@Getter
@Setter
public class Movie {

    //поля
    private int id;
    private String title;
    private String genre;
    private int year;

}
