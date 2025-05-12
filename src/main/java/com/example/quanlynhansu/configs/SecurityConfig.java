package com.example.quanlynhansu.configs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtConfig jwtConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        System.out.println("log_test");
        http
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOriginPatterns(List.of("*"));
                    config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    config.setAllowedHeaders(List.of("*"));
                    config.setAllowCredentials(true);
                    return config;
                }))
                .csrf(customizer -> customizer.disable())
                .addFilterBefore(jwtConfig, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers("/ws/**").permitAll() // Cho phép kết nối WebSocket
                        .requestMatchers(POST, "/user/register").hasAuthority("Admin")
                        .requestMatchers(GET, "/employee/all").hasAnyAuthority("Admin", "HR")
                        .requestMatchers(GET, "/employee/current_user_info").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(PUT, "/employee/update_current_user_info").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(PUT, "/employee/update_password").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(PUT, "/employee/update_avatar").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(PUT, "/employee/delete/{id}").hasAnyAuthority("Admin")
                        .requestMatchers(GET, "/contract/select").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(GET, "/contract/select_by/**").hasAnyAuthority("Admin", "HR")
                        .requestMatchers(POST, "/contract/add").hasAnyAuthority("Admin", "HR")
                        .requestMatchers(PUT, "/contract/update").hasAnyAuthority("Admin", "HR")
                        .requestMatchers(GET, "/work_history/select").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(GET, "/attendance/display/{id}").hasAnyAuthority("Admin", "Manager")
                        .requestMatchers(GET, "/attendance/display").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(POST, "/attendance/add").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(POST, "/work_schedule/create").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(GET, "/work_schedule/display").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(GET, "/work_schedule/current_week").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(GET, "/work_schedule/display_month/{monthYear}").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(POST, "/work_schedule/test_create_fulltime").permitAll()
                        .requestMatchers(POST, "/note/display_note_ca").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(POST, "/note/create").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(GET, "/employee/list_info_chat").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(GET, "/api/online-users").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(GET, "/history").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(POST, "/payroll/summary").permitAll()
                        .requestMatchers(GET, "payroll/payroll_last_month").permitAll()
                        .requestMatchers(GET, "payroll/history_calculate_score").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(new BCryptPasswordEncoder(12));
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

}
