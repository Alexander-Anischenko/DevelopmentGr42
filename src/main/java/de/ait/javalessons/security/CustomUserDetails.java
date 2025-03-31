package de.ait.javalessons.security;

import de.ait.javalessons.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Custom implementation of the {@link UserDetails} interface.
 * This class serves as a bridge between the application's {@link User} entity
 * and Spring Security, providing user-specific logic for authentication and authorization.
 * <p>
 * Key Responsibilities:
 * - Wraps a {@link User} entity to adapt its properties
 * for the {@link UserDetails} interface.
 * - Provides user authorities by converting the roles associated with the {@link User}
 * entity into a {@link Collection} of {@link GrantedAuthority}.
 * - Supplies user credentials, including username and password, as required by the
 * Spring Security framework during the authentication process.
 * <p>
 * Usage:
 * This class is instantiated and used by {@link CustomUserDetailsService}
 * when loading user details from the database for authentication.
 */
public class CustomUserDetails implements UserDetails {

    private final User user; // Поле для хранения объекта User, полученного из базы данных

    // Конструктор, принимает объект User и сохраняет его в поле user
    public CustomUserDetails(User user) {
        this.user = user;
    }

    public User getUser() {  // Метод, позволяющий получить исходный объект User из этого класса
        return user;
    }


    // Возвращает коллекцию прав доступа (авторитетов), которые есть у пользователя.
    // Здесь из ролей пользователя (например, "ROLE_USER") создаются объекты SimpleGrantedAuthority.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Преобразуем Set<String> ролей из сущьности User в поток
        return user.getRoles().stream()
                .map(SimpleGrantedAuthority::new) // Каждую строку превращаем в объект SimpleGrantedAuthority
                .collect(Collectors.toSet()); // Собираем обратно в Set
    }

    @Override // Возвращает пароль пользователя, используется при проверке логина.
    public String getPassword() {
        return user.getPassword();
    }

    @Override // Возвращает имя пользователя (логин), используется для поиска пользователя по имени.
    public String getUsername() {
        return user.getUsername();
    }
}
