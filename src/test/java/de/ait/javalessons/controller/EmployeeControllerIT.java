package de.ait.javalessons.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// Поднимаем полный контекст Spring Boot с использованием случайного порта для тестов
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// Автоматически настраиваем объект MockMvc для имитации HTTP-запросов без запуска реального сервера
@AutoConfigureMockMvc
public class EmployeeControllerIT {

    // Внедряем MockMvc для выполнения HTTP-запросов в тестах
    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Проверяем, что публичный эндпоинт доступен всем и возвращает правильный контент")
    void testGetPublicInfo() throws Exception {
        // Выполняем GET-запрос по URL "/employees/public/info"
        mockMvc.perform(get("/employees/public/info"))
                // Ожидаем, что статус ответа 200 (OK)
                .andExpect(status().isOk())
                // Ожидаем, что ответ содержит точную строку "User Info, public information"
                .andExpect(content().string("User Info, public information"));
    }

    // Группируем тесты для эндпоинта "/employees/user/info" с помощью вложенного класса
    @Nested
    @DisplayName("Тесты для эндпоинта /employees/user/info")
    class UserInfoTests {

        @Test
        @DisplayName("Когда пользователь авторизован с ролью USER, возвращается статус 200 и нужный контент")
        // Эмулируем авторизованного пользователя с ролью USER
        @WithMockUser(username = "testUser", roles = {"USER"})
        void testGetUserInfoAsUser() throws Exception {
            // Выполняем GET-запрос к "/employees/user/info"
            mockMvc.perform(get("/employees/user/info"))
                    // Ожидаем статус 200 (OK)
                    .andExpect(status().isOk())
                    // Ожидаем, что возвращается строка "User Info, secured user information"
                    .andExpect(content().string("User Info, secured user information"));
        }

        @Test
        @DisplayName("Когда пользователь не авторизован, ")
        void testGetUserInfoAsAnonymous() throws Exception {
            // Выполняем GET-запрос к "/employees/user/info" без авторизации
            mockMvc.perform(get("/employees/user/info"))
                    // Ожидаем, что пользователь будет перенаправлен (обычно на страницу логина) со статусом 3xx
                    .andExpect(status().is3xxRedirection());
        }
    }

    // Группируем тесты для эндпоинта "/employees/admin/info" с помощью вложенного класса
    @Nested
    @DisplayName("Тесты для эндпоинта /employees/admin/info")
    class AdminInfoTests {

        @Test
        @DisplayName("Когда пользователь авторизован с ролью ADMIN, возвращается статус 200 и нужный контент")
        // Эмулируем авторизованного пользователя с ролью ADMIN
        @WithMockUser(username = "adminUser", roles = {"ADMIN"})
        void testGetAdminInfoAsAdmin() throws Exception {
            // Выполняем GET-запрос к "/employees/admin/info"
            mockMvc.perform(get("/employees/admin/info"))
                    // Ожидаем статус 200 (OK)
                    .andExpect(status().isOk())
                    // Ожидаем, что возвращается строка "Admin Info, secured admin information"
                    .andExpect(content().string("Admin Info, secured admin information"));
        }

        @Test
        @DisplayName("Когда пользователь не имеет роли ADMIN, возвращается ошибка 403")
        // Эмулируем авторизованного пользователя с ролью USER, который не имеет прав администратора
        @WithMockUser(username = "testUser", roles = {"USER"})
        void testGetAdminInfoAsUser() throws Exception {
            // Выполняем GET-запрос к "/employees/admin/info"
            mockMvc.perform(get("/employees/admin/info"))
                    // Ожидаем, что сервер вернет статус 403 (Forbidden) для пользователя без прав администратора
                    .andExpect(status().isForbidden());
        }

        @Test
        @DisplayName("Когда пользователь не авторизован, возвращается ошибка 401 (или 403)")
        void testGetAdminInfoAsAnonymous() throws Exception {
            // Выполняем GET-запрос к "/employees/admin/info" без авторизации
            mockMvc.perform(get("/employees/admin/info"))
                    // Ожидаем, что пользователь будет перенаправлен (обычно на страницу логина), что выражается статусом 3xx
                    .andExpect(status().is3xxRedirection());
        }
    }
}
