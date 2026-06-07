package com.chema.db.crudapi.auth;

import com.chema.db.crudapi.model.Role;
import com.chema.db.crudapi.model.User;
import com.chema.db.crudapi.repository.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.chema.db.crudapi.security.JwtService;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public void register(AuthRequest request) {

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.USER);

        userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(user.getUsername());

        return new AuthResponse(token);
    }
}
