package de.ait.javalessons.security;

import de.ait.javalessons.model.User;
import de.ait.javalessons.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;// Доступ к репозиторию с данными

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Loads the user details based on the provided username.
     * This method retrieves the user entity from the database and converts it
     * into a {@link UserDetails} implementation for Spring Security.
     * If no user is found for the specified username, an exception is thrown.
     *
     * @param username the username of the user to be loaded
     * @return a {@link UserDetails} object containing user authentication and authorization data
     * @throws UsernameNotFoundException if no user is found for the provided username
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);// загружает пользователя из репозитория
        if( user == null ) { // если не находит юзера по имени, выбрасывает исключение
            log.error("User not found: {}", username);// и пишет лог
            throw new UsernameNotFoundException("User not found");
        }
        return new CustomUserDetails(user);  // Преобразуем объект User в CustomUserDetails,
        // который реализует интерфейс UserDetails, необходимый для работы Spring Security
    }
}
