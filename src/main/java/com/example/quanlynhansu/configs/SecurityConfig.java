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
                .csrf(customizer -> customizer.disable())
                .addFilterBefore(jwtConfig, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request -> request
                        .requestMatchers("/user/login").permitAll()
                        .requestMatchers(POST, "/user/register").hasAuthority("Admin")
                        .requestMatchers(GET, "/employee/all").hasAuthority("Admin")
                        .requestMatchers(GET, "/employee/current_user_info").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(PUT, "/employee/update_current_user_info").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(PUT, "/employee/update_password").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(GET, "/contract/select").hasAnyAuthority("Admin", "Manager", "Employee", "HR")
                        .requestMatchers(GET, "/contract/select_by/**").hasAnyAuthority("Admin", "HR")
                        .requestMatchers(POST, "/contract/add").hasAnyAuthority("Admin", "HR")
                        .requestMatchers(PUT, "/contract/update").hasAnyAuthority("Admin", "HR")
                        .requestMatchers(GET, "/work_history/select").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(GET, "/attendance/display/{id}").hasAnyAuthority("Admin", "Manager")
                        .requestMatchers(GET, "/attendance/display").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
                        .requestMatchers(POST, "/attendance/add").hasAnyAuthority("Admin", "HR", "Manager", "Employee")
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
