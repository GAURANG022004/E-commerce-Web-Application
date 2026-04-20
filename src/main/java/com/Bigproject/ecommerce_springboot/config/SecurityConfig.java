package com.Bigproject.ecommerce_springboot.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public access
                .requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
                // Admin only
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // Retailer and Admin only
                .requestMatchers("/retailer/**").hasAnyRole("RETAILER", "ADMIN")
                .requestMatchers("/products/new", "/products/save", "/products/edit/**", "/products/delete/**").hasAnyRole("RETAILER", "ADMIN")
                // Customer can view products
                .requestMatchers("/products", "/products/search", "/products/filter").hasAnyRole("CUSTOMER", "RETAILER", "ADMIN")
                // Customer cart operations
                .requestMatchers("/cart", "/cart/**", "/add/**").hasAnyRole("CUSTOMER", "ADMIN")
                // Customer checkout and payment
                .requestMatchers("/checkout", "/checkout/**", "/payment", "/payment/**", "/orders", "/orders/**").hasAnyRole("CUSTOMER", "ADMIN")
                // Any other request requires authentication
                .anyRequest().authenticated()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/access-denied")
            )
            .csrf(csrf -> csrf.disable());
        
        return http.build();
    }
}
