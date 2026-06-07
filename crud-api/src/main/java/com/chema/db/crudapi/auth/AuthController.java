package com.chema.db.crudapi.auth;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public Map<String, String> AuthResponse(@Valid @RequestBody AuthRequest request) {
        authService.register(request);
        return Map.of("message", "User registered successfully");
    }

    @PostMapping("/login")
    public AuthResponse login(@Valid @RequestBody AuthRequest request) {
        authService.login(request);

        return authService.login(request);
    }
}
