package com.chema.db.crudapi.security;

import com.chema.db.crudapi.repository.UserRepository;
import com.chema.db.crudapi.model.User;

// Clases necesarias para trabajar con filtros HTTP
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Clases de Spring Security
import org.jspecify.annotations.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

// Registra el filtro como Bean de Spring
import org.springframework.stereotype.Component;

// Filtro que se ejecuta una sola vez por petición
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.List;

// Spring detecta automáticamente este filtro
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Servicio encargado de trabajar con JWT
    private final JwtService jwtService;

    // Acceso a la tabla de usuarios
    private final UserRepository userRepository;

    // Inyección de dependencias
    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    // Método ejecutado automáticamente en cada request
    @Override
    protected void doFilterInternal( @NonNull HttpServletRequest request,
                                     @NonNull HttpServletResponse response,
                                     @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        // Obtiene la cabecera Authorization
        String authHeader = request.getHeader("Authorization");

        // Si no existe Authorization o no empieza por "Bearer "
        // deja pasar la petición sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }

        // Elimina "Bearer " y se queda solo con el token
        String token = authHeader.substring(7);

        // Extrae el username guardado dentro del JWT
        String username = jwtService.extractUsername(token);

        // Si existe username y nadie está autenticado todavía
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // Busca usuario en base de datos
            User user = userRepository.findByUsername(username)
                    .orElse(null);

            // Comprueba:
            // 1. que el usuario exista
            // 2. que el token sea válido
            if (user != null &&
                    jwtService.isTokenValid(token, user.getUsername())) {

                // Crea un objeto de autenticación para Spring Security
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(

                                // Usuario autenticado
                                user.getUsername(),

                                // Contraseña (no hace falta porque ya validamos JWT)
                                null,

                                // Lista de roles/permisos
                                // Sin permisos Collections.emptyList()
                                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
                        );

                // Añade detalles de la petición actual
                authToken.setDetails(
                        new WebAuthenticationDetailsSource()
                                .buildDetails(request)
                );

                // Guarda la autenticación en el contexto de Spring
                // A partir de aquí Spring considera al usuario autenticado
                SecurityContextHolder
                        .getContext()
                        .setAuthentication(authToken);
            }
        }

        // Continúa con el siguiente filtro o controller
        filterChain.doFilter(request, response);
    }
}
