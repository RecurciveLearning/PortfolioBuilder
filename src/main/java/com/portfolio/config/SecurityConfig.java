package com.portfolio.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${admin.username:himanshu}")
    private String adminUsername;

    @Value("${admin.password:himanshu1997}")
    private String adminPassword;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        // Admin pages
                        .requestMatchers("/admin/**").hasRole("ADMIN")

                        // Public endpoints
                        .requestMatchers(
                                "/", "/css/**", "/js/**", "/images/**", "/resume/**",
                                "/contact", "/meeting/request", "/blogs/**", "/judge0",
                                "/certifications", "/admin/certification/list",
                                "/certifications/**", "/api/jdoodle/**",
                                "/notes/**"  // ✅ Allow Smart Notepad public access
                        ).permitAll()

                        // Everything else requires authentication
                        .anyRequest().authenticated()
                )
                // Login
                .formLogin(form -> form
                        .loginPage("/admin/login")
                        .loginProcessingUrl("/admin/login")
                        .defaultSuccessUrl("/admin/dashboard", true)
                        .permitAll()
                )
                // Logout
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/admin/logout"))
                        .logoutSuccessUrl("/")
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        .permitAll()
                )
                // ✅ CSRF configuration
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/contact",
                                "/meeting/request",
                                "/api/chat/**",
                                "/admin/certification/list",
                                "/api/jdoodle/**",
                                "/notes/**"
                        )
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username(adminUsername)
                .password(passwordEncoder().encode(adminPassword))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
