package com.school.ecommerce.config;

import com.school.ecommerce.model.Role;
import com.school.ecommerce.repository.UserRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, UserRepository userRepository) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Deshabilitado para simplificar pruebas iniciales
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/error", "/login", "/register", "/css/**", "/images/**", "/js/**",
                        "/uploads/**", "/actuator/**", "/colecciones", "/creadores", "/tiendas", "/tienda/**",
                        "/favoritos", "/product/**", "/api/**", "/checkout", "/success").permitAll()
                .requestMatchers("/admin/**").access((authenticationSupplier, context) -> {
                    Authentication authentication = authenticationSupplier.get();
                    boolean isAdmin = authentication != null
                            && authentication.isAuthenticated()
                            && userRepository.findByEmail(authentication.getName())
                                    .map(user -> user.getRole() == Role.ADMIN)
                                    .orElse(false);
                    return new AuthorizationDecision(isAdmin);
                })
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .successHandler((request, response, authentication) -> {
                    String redirectUri = request.getParameter("redirect_uri");
                    if (redirectUri != null && !redirectUri.isBlank()) {
                        response.sendRedirect(redirectUri);
                    } else {
                        response.sendRedirect("/dashboard");
                    }
                })
                .usernameParameter("email")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
                .permitAll()
            );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
