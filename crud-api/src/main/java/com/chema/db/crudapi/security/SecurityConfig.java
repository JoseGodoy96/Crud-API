package com.chema.db.crudapi.security;

// Permite registrar Beans y clases de configuración en Spring
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Objeto principal para configurar Spring Security
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// Configuración de sesiones HTTP
import org.springframework.security.config.http.SessionCreationPolicy;

// Interfaz para codificar contraseñas
import org.springframework.security.crypto.password.PasswordEncoder;

// Implementación BCrypt para almacenar contraseñas de forma segura
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// Define las reglas de seguridad de la aplicación
import org.springframework.security.web.SecurityFilterChain;

// Filtro estándar de login de Spring Security
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// Clase de configuración de Spring Security
@Configuration
public class SecurityConfig {

    // Filtro JWT personalizado que validará los tokens
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    // Inyección de dependencias
    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    // Bean principal de configuración de seguridad
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {

        http

                // Desactiva la protección CSRF
                // En APIs REST con JWT normalmente no es necesaria
                .csrf(csrf -> csrf.disable())

                // Configura la gestión de sesiones
                .sessionManagement(session ->

                        // Indica que NO se usarán sesiones HTTP
                        // Cada request deberá autenticarse mediante JWT
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                // Configura qué endpoints son accesibles
                .authorizeHttpRequests(auth -> auth

                        // Permite acceso público a todos los endpoints de autenticación
                        .requestMatchers("/auth/**").permitAll()

                        // Permite acceder a Swagger sin necesidad de autenticarse
                        .requestMatchers(
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()

                        // Solo usuarios con rol USER o ADMIN pueden acceder
                        .requestMatchers("/api/tasks/**")
                        .hasAnyRole("USER", "ADMIN")

                        // Cualquier endpoint que no haya sido configurado anteriormente
                        // requiere que el usuario esté autenticado
                        .anyRequest().authenticated()
                )

                // Inserta nuestro filtro JWT antes del filtro
                // estándar UsernamePasswordAuthenticationFilter
                .addFilterBefore(

                        jwtAuthenticationFilter,

                        UsernamePasswordAuthenticationFilter.class
                );

        // Construye y devuelve la configuración final
        return http.build();
    }

    // Bean que Spring utilizará para cifrar contraseñas
    @Bean
    public PasswordEncoder passwordEncoder() {

        // BCrypt es el algoritmo recomendado por Spring
        return new BCryptPasswordEncoder();
    }
}