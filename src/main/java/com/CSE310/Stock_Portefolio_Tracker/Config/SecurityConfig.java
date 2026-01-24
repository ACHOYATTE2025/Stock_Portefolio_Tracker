package com.CSE310.Stock_Portefolio_Tracker.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final PasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain FilterChain (HttpSecurity httpSecurity) throws Exception{

        return httpSecurity
            .csrf(csrf -> csrf.disable())
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint((req, res, e) -> {
                res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                res.getWriter().write(
                    "{\"status\":401,\"message\":\"UNAUTHORIZED\",\"error\":\"BAD CREDENTIALS\"}"
                );
            })
        )
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/stockportefoliotracker/v1/login",
                             "/api/stockportefoliotracker/v1/register"
                             ).permitAll()
            .anyRequest().authenticated()
        )
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        )

            .build();
    }

    
@Bean
public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }



@Bean
public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService) throws Exception {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }


}
