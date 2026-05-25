package com.management.ticketasset.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import org.springframework.security.config.Customizer;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.servlet.util.matcher.PathPatternRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Enable CORS based on CorsConfig
            .cors(Customizer.withDefaults())
            // Disable CSRF for REST API testing simplicity
            .csrf(AbstractHttpConfigurer::disable)
            
            // Allow frame options for H2 Console
            .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()))
            
            // Session Management: Stateless
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Authorize HTTP requests
            .authorizeHttpRequests(auth -> auth
            // Allow H2 Console requests
            .requestMatchers(PathPatternRequestMatcher.withDefaults().matcher("/h2-console/**")).permitAll()
            
            // Allow anyone to authenticate / check connection
            .requestMatchers("/api/auth/me").authenticated()
            
            // Role-based restrictions for Assets
            .requestMatchers(HttpMethod.GET, "/api/assets", "/api/assets/**").hasAnyRole("ADMIN", "EMPLOYEE")
            .requestMatchers(HttpMethod.POST, "/api/assets", "/api/assets/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.PUT, "/api/assets", "/api/assets/**").hasRole("ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/api/assets", "/api/assets/**").hasRole("ADMIN")
            
            // Role-based restrictions for Tickets (Both can access, control logic is handled in Service)
            .requestMatchers("/api/tickets", "/api/tickets/**").hasAnyRole("ADMIN", "EMPLOYEE")
            
            // All other requests require authentication
            .anyRequest().authenticated()
        )
            
            // Enable HTTP Basic Authentication (excellent for Postman testing)
            .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}
