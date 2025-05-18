package com.danghieu99.monolith.security.config;

import com.danghieu99.monolith.security.config.auth.AuthTokenEntryPoint;
import com.danghieu99.monolith.security.config.auth.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthTokenEntryPoint authTokenEntryPoint;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        List<String> allowedHeaders = new ArrayList<>();
        allowedHeaders.add("X-Requested-With");
        allowedHeaders.add("Origin");
        allowedHeaders.add("Accept");
        allowedHeaders.add("Accept-Encoding");
        allowedHeaders.add("Authorization");
        allowedHeaders.add("Access-Control-Allow-Credentials");
        allowedHeaders.add("Access-Control-Allow-Headers");
        allowedHeaders.add("Access-Control-Allow-Methods");
        allowedHeaders.add("Access-Control-Allow-Origin");
        allowedHeaders.add("Access-Control-Expose-Headers");
        allowedHeaders.add("Access-Control-Max-Age");
        allowedHeaders.add("Access-Control-Request-Headers");
        allowedHeaders.add("Access-Control-Request-Method");
        allowedHeaders.add("Age");
        allowedHeaders.add("Allow");
        allowedHeaders.add("Alternates");
        allowedHeaders.add("Content-Range");
        allowedHeaders.add("Content-Type");
        allowedHeaders.add("Content-Disposition");
        allowedHeaders.add("Connection");
        allowedHeaders.add("Cookies");
        allowedHeaders.add("Cookie");
        allowedHeaders.add("Referer");
        allowedHeaders.add("Host");
        allowedHeaders.add("withCredentials");

        List<String> allowedMethods = new ArrayList<>();
        allowedMethods.add("GET");
        allowedMethods.add("POST");
        allowedMethods.add("PUT");
        allowedMethods.add("DELETE");
        allowedMethods.add("OPTIONS");
        allowedMethods.add("PATCH");

        List<String> exposedHeaders = new ArrayList<>();
        exposedHeaders.add("Cookie");
        exposedHeaders.add("Cookies");
        exposedHeaders.add("application/json");

        List<String> origins = new ArrayList<>();
        origins.add("http://localhost:3000");

        corsConfiguration.setAllowedHeaders(allowedHeaders);
        corsConfiguration.setAllowedOrigins(origins);
        corsConfiguration.setAllowedMethods(allowedMethods);
        corsConfiguration.setExposedHeaders(exposedHeaders);
        corsConfiguration.setAllowPrivateNetwork(true);
        corsConfiguration.setAllowCredentials(true);

        http
//                .requiresChannel(channel -> channel.anyRequest().requiresSecure())
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(request -> corsConfiguration))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/user/**").authenticated()
                        .requestMatchers("/api/v1/seller/**").hasRole("SELLER")
                        .requestMatchers("/api/v1/admin/**").hasRole("ADMIN")
                        .requestMatchers("/api/v1/**").permitAll()
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/swagger-ui.html/**", "/v3/api-docs/**").permitAll()
                        .requestMatchers("/swagger-resources/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .anyRequest().denyAll()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authTokenEntryPoint)
                );
        http.addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}