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
            String username = "ADMIN_USER";
            String rawPassword = "ADMIN_PASSWORD";

            if (userRepository.findByUsername(username).isEmpty()) {
                User user = new User();
                user.setUsername(username);
                user.setPassword(passwordEncoder.encode(rawPassword));
                user.setRole("ROLE_USER");
                userRepository.save(user);
                System.out.println("User testing created: " + username + " / " + rawPassword);
            }
        };
    }
}
