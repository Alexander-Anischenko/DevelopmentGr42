package de.ait.javalessons.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Car { // Классописывает сущбность, которую можно использовать в базе данных

    @Id // Анотация ID говорит о том, что id будут генерироваться автоматически
    //поля
    // Идентификатор автомобиля (неизменяемый после создания)
    private  String id;
    private String name;

    public Car() {
        // Конструктор по умолчанию, необходим для JPA
        // Default constructor, required for JPA
    }

    //конструктор
    public Car(String id, String name) {
        this.id = id;
        this.name = name;
    }

    //геттеры
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    //сеттеры
    public void setName(String name) {
        this.name = name;
    }

    public void setId(String id) {
        this.id = id;
    }
}
