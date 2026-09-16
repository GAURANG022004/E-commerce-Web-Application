package com.Bigproject.ecommerce_springboot.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.Bigproject.ecommerce_springboot.Repository.UserRepository;
import com.Bigproject.ecommerce_springboot.entity.User;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createAdmin(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.email}") String adminEmail,
            @Value("${admin.password}") String adminPassword) {

        return args -> {

            User existingAdmin = userRepository.findByEmail(adminEmail);

            if (existingAdmin == null) {

                User admin = new User();

                admin.setUsername("admin");
                admin.setEmail(adminEmail);
                admin.setUserpassword(passwordEncoder.encode(adminPassword));
                admin.setRole("ADMIN");
                admin.setStatus("APPROVED");

                userRepository.save(admin);

                System.out.println("ADMIN account created successfully.");
            } else {
                System.out.println("ADMIN EMAIL IN DB: " + existingAdmin.getEmail());
                System.out.println("ADMIN ROLE IN DB: " + existingAdmin.getRole());
                System.out.println("PASSWORD MATCH: " +
                        passwordEncoder.matches(adminPassword, existingAdmin.getUserpassword()));
            }
        };
    }
}