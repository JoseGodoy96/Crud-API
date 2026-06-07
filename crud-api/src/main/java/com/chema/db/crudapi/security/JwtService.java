package com.chema.db.crudapi.security;

// Librería JWT para construir tokens
import io.jsonwebtoken.Jwts;

// Utilidad para generar claves criptográficas seguras
import io.jsonwebtoken.security.Keys;

// Registra esta clase como un Bean gestionado por Spring
import org.springframework.stereotype.Service;

import java.util.Date;

// Tipo utilizado para firmar y validar el JWT
import javax.crypto.SecretKey;


// Servicio encargado de generar tokens JWT
@Service
public class JwtService {

    /* Clave secreta utilizada para firmar los tokens
    Debe ser suficientemente larga para HS256
    En producción debería almacenarse en variables de entorno */
    private static final String SECRET_KEY =
            "my-super-secret-key-for-jwt-authentication-123456";

    /* Tiempo de vida del token en milisegundos
     1000 ms * 60 seg * 60 min = 1 hora */
    private static final long EXPIRATION_TIME = 1000 * 60 * 60; // 1 hora

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    // Genera un JWT para el usuario recibido como parámetro
    public String generateToken(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSigningKey())
                .compact();
    }

    // Metodo para extraer el Token y poder leerlo
    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    // Validador de que el token y el nombre son correctos
    public boolean isTokenValid(String token, String username) {

        String extractedUsername = extractUsername(token);

        return extractedUsername.equals(username);
    }
}
