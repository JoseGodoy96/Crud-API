package com.chema.db.crudapi.auth;

// Clases del proyecto
import com.chema.db.crudapi.model.Role;
import com.chema.db.crudapi.model.User;
import com.chema.db.crudapi.repository.UserRepository;
import com.chema.db.crudapi.security.JwtService;

// JUnit
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

// Mockito
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Spring Security
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

// Métodos de validación de JUnit
import static org.junit.jupiter.api.Assertions.*;

// Métodos auxiliares de Mockito
import static org.mockito.Mockito.*;

// Activa Mockito para esta clase de test
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    // Repositorio simulado (no accede a PostgreSQL real)
    @Mock
    private UserRepository userRepository;

    // Codificador de contraseñas simulado
    @Mock
    private PasswordEncoder passwordEncoder;

    // Servicio JWT simulado
    @Mock
    private JwtService jwtService;

    // Clase real que queremos probar
    // Mockito inyectará automáticamente los mocks anteriores
    @InjectMocks
    private AuthService authService;

    // Comprueba que el registro de usuarios funciona correctamente
    @Test
    void shouldRegisterUser() {

        // Simula la petición de registro
        AuthRequest request = new AuthRequest();
        request.setUsername("jose");
        request.setPassword("password123");

        // Cuando se codifique la contraseña
        // devolver "hashedPassword"
        when(passwordEncoder.encode("password123"))
                .thenReturn("hashedPassword");

        // Ejecuta el método real
        authService.register(request);

        // Comprueba que la contraseña fue codificada
        verify(passwordEncoder, times(1))
                .encode("password123");

        // Comprueba que el usuario fue guardado
        verify(userRepository, times(1))
                .save(any(User.class));
    }

    // Comprueba que el login genera un JWT válido
    @Test
    void shouldLoginUserAndReturnToken() {

        // Simula la petición de login
        AuthRequest request = new AuthRequest();
        request.setUsername("jose");
        request.setPassword("password123");

        // Simula un usuario existente en la base de datos
        User user = new User();
        user.setId(1L);
        user.setUsername("jose");
        user.setPassword("hashedPassword");
        user.setRole(Role.USER);

        // Cuando se busque el usuario "jose"
        when(userRepository.findByUsername("jose"))
                .thenReturn(Optional.of(user));

        // La contraseña introducida coincide con la almacenada
        when(passwordEncoder.matches(
                "password123",
                "hashedPassword"))
                .thenReturn(true);

        // El servicio JWT devuelve un token falso
        when(jwtService.generateToken("jose"))
                .thenReturn("fake-jwt-token");

        // Ejecuta el login
        AuthResponse response =
                authService.login(request);

        // Comprueba que la respuesta existe
        assertNotNull(response);

        // Comprueba que el token es el esperado
        assertEquals(
                "fake-jwt-token",
                response.getToken()
        );

        // Verifica que se realizaron las llamadas esperadas
        verify(userRepository, times(1))
                .findByUsername("jose");

        verify(passwordEncoder, times(1))
                .matches(
                        "password123",
                        "hashedPassword"
                );

        verify(jwtService, times(1))
                .generateToken("jose");
    }

    // Comprueba que el login falla cuando la contraseña es incorrecta
    @Test
    void shouldThrowExceptionWhenPasswordIsInvalid() {

        // Simula petición con contraseña incorrecta
        AuthRequest request = new AuthRequest();
        request.setUsername("jose");
        request.setPassword("wrongPassword");

        // Simula usuario existente
        User user = new User();
        user.setUsername("jose");
        user.setPassword("hashedPassword");
        user.setRole(Role.USER);

        // Usuario encontrado en la base de datos
        when(userRepository.findByUsername("jose"))
                .thenReturn(Optional.of(user));

        // La contraseña NO coincide
        when(passwordEncoder.matches(
                "wrongPassword",
                "hashedPassword"))
                .thenReturn(false);

        // Esperamos una excepción
        assertThrows(
                RuntimeException.class,
                () -> authService.login(request)
        );

        // Comprueba que nunca se generó un token JWT
        verify(jwtService, never())
                .generateToken(anyString());
    }
}