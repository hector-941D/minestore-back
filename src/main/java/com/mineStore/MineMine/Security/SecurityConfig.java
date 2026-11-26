package com.mineStore.MineMine.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> {})
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth

                .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html", "/error").permitAll()
                .requestMatchers("/api/v1/auth/**", "/api/v2/auth/**", "/api/health**") .permitAll()

                .requestMatchers(HttpMethod.POST,
                        "/api/v1/usuarios/**", "/api/v1/roles/**", "/api/v1/regiones/**", "/api/v1/comunas/**",
                        "/api/v2/usuarios/**", "/api/v2/roles/**", "/api/v2/regiones/**", "/api/v2/comunas/**"
                ).permitAll()

                .requestMatchers(HttpMethod.GET,
                        "/api/v1/productos/**", "/api/v1/categorias/**", "/api/v1/imagenes/**",
                        "/api/v1/regiones/**", "/api/v1/comunas/**", "/api/v1/roles/**",
                        "/api/v1/tipos/**", "/api/v1/metodos-pago/**", "/api/v1/metodos-envio/**",
                        "/api/v2/productos/**", "/api/v2/categorias/**", "/api/v2/imagenes/**",
                        "/api/v2/regiones/**", "/api/v2/comunas/**", "/api/v2/roles/**",
                        "/api/v2/tipos/**", "/api/v2/metodos-pago/**", "/api/v2/metodos-envio/**"
                ).permitAll()

                .requestMatchers(HttpMethod.POST,
                        "/api/v1/ventas/**", "/api/v1/detalles/**",
                        "/api/v2/ventas/**", "/api/v2/detalles/**"
                ).hasAnyRole("CLIENTE", "ADMIN")

                .requestMatchers(HttpMethod.GET,
                        "/api/v1/ventas/**", "/api/v1/detalles/**",
                        "/api/v2/ventas/**", "/api/v2/detalles/**"
                ).hasAnyRole("CLIENTE", "ADMIN")
                
                .anyRequest().hasRole("ADMIN")
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}