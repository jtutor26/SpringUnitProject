package com.example.unitprojectspring.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // This tells Spring to apply these web security rules
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 1. URL Authorization Rules
                .authorizeHttpRequests(auth -> auth
                        // Allow public access to registration, login, and your static files (CSS/JS)
                        .requestMatchers("/register", "/login", "/css/**", "/js/**", "/images/**", "/test-api.html").permitAll()
                        // Require the user to be logged in for absolutely every other request
                        .anyRequest().authenticated()
                )
                // 2. Custom Login Form Setup
                .formLogin(form -> form
                        .loginPage("/login") // Tells Spring to look for your custom Thymeleaf template mapped to /login
                        .defaultSuccessUrl("/projects", true) // Redirect here after a successful login
                        .permitAll()
                )
                // 3. Logout Setup
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout") // Redirect back to login with a success parameter
                        .invalidateHttpSession(true) // Destroys their session
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .csrf(csrf -> csrf.disable());

        return http.build();
    }
}
