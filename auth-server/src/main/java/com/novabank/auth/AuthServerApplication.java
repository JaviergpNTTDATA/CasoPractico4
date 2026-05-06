package com.novabank.auth;

import com.novabank.auth.model.User;
import com.novabank.auth.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthServerApplication.class, args);
    }

    @Bean
    public CommandLineRunner initTestUser(UserRepository userRepository,
                                          PasswordEncoder passwordEncoder) {
        return args -> {
            String username = "testuser";
            String rawPassword = "password"; // contraseña en texto plano

            // Si ya existe, no lo duplicamos
            if (userRepository.findByUsername(username).isEmpty()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(rawPassword));
                user.setRole("ROLE_USER");
                userRepository.save(user);
                System.out.println("Usuario de prueba creado: " + username + " / " + rawPassword);
            }
        };
    }
}
