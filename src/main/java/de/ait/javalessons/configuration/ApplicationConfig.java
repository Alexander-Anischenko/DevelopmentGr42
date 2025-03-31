package de.ait.javalessons.configuration;

import de.ait.javalessons.security.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration class for the application.
 * Класс конфигурации для приложения.
 */
@Configuration
public class ApplicationConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public ApplicationConfig(CustomUserDetailsService customUserDetailsService) {
        // Injecting CustomUserDetailsService.
        // Внедрение CustomUserDetailsService.
        this.customUserDetailsService = customUserDetailsService;
    }

    /**
     * Creates a password encoder bean.
     * Создает бин для кодирования паролей.
     * @return The password encoder.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        // Using BCryptPasswordEncoder for password encoding.
        // Использование метода BCryptPasswordEncoder для кодирования паролей.
        return new BCryptPasswordEncoder();
    }

    /**
     * Creates a DaoAuthenticationProvider bean.
     * Создает бин DaoAuthenticationProvider.
     * @return The DaoAuthenticationProvider.
     */
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        // Setting the user details service.
        // Установка сервиса для получения пользовательских данных.
        daoAuthenticationProvider.setUserDetailsService(customUserDetailsService);
        // Setting the password encoder.
        // Установка кодировщика паролей.
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }
}
