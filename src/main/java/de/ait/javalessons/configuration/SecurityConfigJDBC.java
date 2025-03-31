package de.ait.javalessons.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;


/**
 * Security configuration class for integrating Spring Security with JDBC-based authentication.
 * This configuration provides bean definitions and methods to secure the application
 * using JDBC for user details management and authentication.
 * Класс конфигурации безопасности для интеграции Spring Security с аутентификацией на основе JDBC.
 * Эта конфигурация предоставляет определения бобов и методы для защиты приложения.
 * использующего JDBC для управления данными пользователей и аутентификации.
 */
//@Configuration // Указываем, что данный класс является конфигурационным и содержит определения бобов
//@EnableWebSecurity // Включаем полную поддержку безопасности для веб-приложения веб-безопасности Spring Security
public class SecurityConfigJDBC {

    /**
     //Создает bean для кодирования паролей с использованием BCrypt.
     //BCrypt - современный алгоритм хеширования паролей с встроенной защитой от радужных таблиц.

     //@return Encoder для безопасного хеширования паролей


    @Bean // Создаем боб PasswordEncoder, который будет использоваться для шифрования паролей
    public PasswordEncoder passwordEncoder() {
        // Создает кодировщик паролей с использованием BCrypt
        // Генерирует случайную "соль" для каждого пароля, усложняя взлом
        return new BCryptPasswordEncoder();
    }


     // Создает менеджер пользователей, работающий через JDBC (базу данных).
     // Позволяет загружать и управлять пользователями, используя стандартные таблицы базы данных.

     //@param dataSource Источник данных для подключения к базе
     // @return Менеджер для работы с пользователями через базу данных

    @Bean
    public JdbcUserDetailsManager jdbcUserDetailsManager(DataSource dataSource) {
        // Создает менеджер, который будет читать данные пользователей из базы
        return new JdbcUserDetailsManager(dataSource);
    }



     // Создает провайдера аутентификации, который объединяет
     // менеджер пользователей и кодировщик паролей.

     //@param jdbcUserDetailsManager Менеджер для загрузки данных пользователей
     //@param passwordEncoder Кодировщик для проверки паролей
     // @return Настроенный провайдер аутентификации

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            JdbcUserDetailsManager jdbcUserDetailsManager,
            PasswordEncoder passwordEncoder
    ) {
        // Создаем провайдер для проверки пользователей
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();

        // Устанавливаем кодировщик паролей для проверки credentials
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);

        // Устанавливаем сервис для загрузки данных пользователей
        daoAuthenticationProvider.setUserDetailsService(jdbcUserDetailsManager);

        return daoAuthenticationProvider;
    }


     // Настраивает цепочку фильтров безопасности для всего приложения.
     // Определяет правила авторизации, аутентификации и доступа к различным URL.

     // @param http Объект для построения конфигурации безопасности
     // @param daoAuthenticationProvider Провайдер для аутентификации
     // @return Сконфигурированная цепочка фильтров безопасности
     // @throws Exception При ошибках конфигурации

    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            DaoAuthenticationProvider daoAuthenticationProvider
    ) throws Exception {
        http
                // Регистрируем провайдер аутентификации
                .authenticationProvider(daoAuthenticationProvider)
                // Отключаем CSRF защиту (обычно для тестирования/разработки)
                .csrf(AbstractHttpConfigurer::disable)
                // Настраиваем правила авторизации для различных URL
                .authorizeHttpRequests(auth -> auth
                        // Доступ только для администраторов
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        // Доступ только для обычных пользователей
                        .requestMatchers("/user/**").hasRole("USER")
                        // Публичные URL, доступные всем без авторизации
                        .requestMatchers("/", "/login/**", "/public/**", "/h2-console/**", "/swagger-ui/**").permitAll()
                        // Все остальные URL(запросы) требуют авторизации
                        .anyRequest().authenticated()
                )
                // Разрешаем встраивание фреймов для консоли H2
                .headers(headers -> headers.frameOptions().sameOrigin())
                // Используем стандартную форму логина
                .formLogin(Customizer.withDefaults())
                // Настраиваем выход из системы
                .logout(logout -> logout.permitAll());

        return http.build();
    } */
}
