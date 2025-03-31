package de.ait.javalessons.utils;

import de.ait.javalessons.model.User;
import de.ait.javalessons.repositories.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Class responsible for initializing user data in the application.
 * It ensures that default "admin" and "user" accounts are created in the
 * database if they do not already exist. Useful for setting up initial data
 * during application startup.
 *
 * Key Features:
 * - Creates an "admin" user with "ROLE_ADMIN" if not present in the database.
 * - Creates a "user" user with "ROLE_USER" if not present in the database.
 * - Encodes passwords for the created users using a provided {@link PasswordEncoder}.
 *
 * Dependencies:
 * - {@link UserRepository}: Used for accessing and persisting user data.
 * - {@link PasswordEncoder}: Used for encoding user passwords before saving.
 */

@Component
public class UserDataInitializer {

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserDataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostConstruct
    public void init(){
        if(userRepository.findByUsername("admin") == null){
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("adminpass"));
            admin.getRoles().add("ROLE_ADMIN");
            userRepository.save(admin);
        }

        if(userRepository.findByUsername("user") == null){
            User user = new User();
            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("userpass"));
            user.getRoles().add("ROLE_USER");
            userRepository.save(user);
        }
    }
}
