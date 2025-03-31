package de.ait.javalessons.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class BcryptUtil { //утилита, работающая с системой шифрования информации
    public static void main(String[] args) {
        // Создаем объект класса BCryptPasswordEncoder
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "adminpass"; // Создаем перемннную, в которой хранится пароль
        // Создаем переменную, в которой храниться закодированный с помощью метеода encode введенный ранее пароль
        String encodetPassword = encoder.encode(password);
        System.out.println(password + "-->"+ encodetPassword); // Визуализация переменной
    }
}

// мой userpass-->$2a$10$JJQxglKNrNTF7qlnxWe1C.ZeF7Sc/Ekrjm3lAdMMpkzm6yKp/n5ne
// Игоря //userpass-->$2a$10$XTYrfRQzPUDsJ.smAKwRougTNtZ80Ak267mGWnaYYQMZG9KaZD2jy

// мой adminpass-->$2a$10$DeMa2GnaqTR6FOyq.l5X7.OUSw2d0LTWg42wOSLMwH0wKISwISZpu
// Игоря adminpass-->$2a$10$pSM3dZIRXDX9qX2R94tF1ehbH6HcKGwApQJDwl3NmC8bvmlIbmqrK
