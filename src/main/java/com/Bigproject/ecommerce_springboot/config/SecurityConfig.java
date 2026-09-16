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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

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
    public AuthenticationSuccessHandler customerAuthenticationSuccessHandler() {

        return (request, response, authentication) -> {

            if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                response.sendRedirect("/admin/dashboard");
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_RETAILER"))) {
                response.sendRedirect("/retailer/dashboard");
            } else if (authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_CUSTOMER"))) {
                response.sendRedirect("/customer/dashboard");
            } else {
                response.sendRedirect("/login");
            }

        };
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/login", "/register",
                                "/css/**", "/js/**", "/images/**")
                        .permitAll()

                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        .requestMatchers("/retailer/**").hasAnyRole("RETAILER", "ADMIN")

                        .requestMatchers("/products/new", "/products/save",
                                "/products/edit/**", "/products/delete/**")
                        .hasAnyRole("RETAILER", "ADMIN")

                        .requestMatchers("/products", "/products/search",
                                "/products/filter")
                        .hasAnyRole("CUSTOMER", "RETAILER", "ADMIN")

                        .requestMatchers("/cart", "/cart/**", "/add/**")
                        .hasAnyRole("CUSTOMER", "ADMIN")

                        .requestMatchers("/checkout", "/checkout/**",
                                "/payment", "/payment/**",
                                "/orders", "/orders/**")
                        .hasAnyRole("CUSTOMER", "ADMIN")

                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .successHandler(customerAuthenticationSuccessHandler())
                        .failureHandler((request, response, exception) -> {
                            System.out.println("LOGIN FAILED: " + exception.getClass().getName());
                            System.out.println("LOGIN FAILED MESSAGE: " + exception.getMessage());
                            response.sendRedirect("/login?error=true");
                        })
                        .permitAll())

                .logout(logout -> logout
                        .logoutSuccessUrl("/login")
                        .permitAll())

                .exceptionHandling(exception -> exception.accessDeniedPage("/access-denied"))

                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}