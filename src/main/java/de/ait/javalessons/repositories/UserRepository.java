package de.ait.javalessons.repositories;

import de.ait.javalessons.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing {@link User} entities.
 * Extends JpaRepository to provide standard CRUD operations and JPA-specific functionality.
 *
 * Custom queries:
 * - Provides a method to find a user by their username.
 *
 * Usage:
 * Designed to work with the {@link User} entity and the underlying database.
 * Supports integration with Spring Data's repository abstraction for data access.
 */
public interface UserRepository extends JpaRepository<User, Long> {

    // Прописывая с подсказками название метода мы не делаем реализацию, она делается автоматически
    //(Магия Spring Boot) нужно правильно прописать название метода
    User findByUsername(String username);
}
