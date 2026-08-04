package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.demo.entity.User;
import com.example.demo.entity.Role;
import com.example.demo.entity.UserStatus;
import com.example.demo.repositories.UserRepository;

@Component
public class CustomerInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomerInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String email = "john.doe@example.com";

        User user = userRepository.findByEmail(email).orElseGet(() -> {
            User newUser = new User();
            newUser.setEmail(email);
            return newUser;
        });

        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPasswordHash(passwordEncoder.encode("buyer123"));
        user.setMobile("9876543210");
        user.setCity("Mumbai");
        user.setState("Maharashtra");
        user.setRole(Role.CUSTOMER);
        user.setStatus(UserStatus.ACTIVE);

        userRepository.save(user);
        System.out.println("Default Customer initialized successfully with email: " + email);
    }
}
