package de.ait.javalessons.model;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

// С помощью аннотаций создаем класс User
@Entity // Аннотация говорит, что это сущность JPA, и она будет сохраняться в БД
@Table(name = "users") // Указываем имя таблицы в базе данных — users
@Getter // Lombok: автоматически создаёт геттеры для всех полей
@Setter // Lombok: автоматически создаёт сеттеры для всех полей
@NoArgsConstructor // Lombok: автоматически создаёт конструктор без аргументо
@AllArgsConstructor // Lombok: автоматически создаёт конструктор со всеми аргументами
@Builder // Lombok: позволяет использовать шаблон Builder для создания объектов User
public class User {

    //поля
    @Id // Обозначает, что это первичный ключ
    @GeneratedValue(strategy = GenerationType.AUTO)  // Стратегия генерации ID (автоинкремент)
    private Long id;

    // Поле username — уникальное и не может быть пустым
    @Column(unique = true, nullable = false) // Создаёт колонку с ограничением уникальности и обязательным значением
    private String username;

    // Поле password — не может быть пустым
    @Column(nullable = false)
    private String password;

    // Поле roles — набор ролей пользователя, хранимых в отдельной таблице
    @ElementCollection(fetch = FetchType.EAGER)
    // Указывает таблицу, где будет храниться эта коллекция (в данном случае roles) с именем roles и внешним ключом user_id из таблицы users
    @CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
    @Column(name = "role", nullable = false) // Имя таблицы role один пользователь может включать в себя 2 и блоее роли
    private Set<String> roles = new HashSet<>();

}
