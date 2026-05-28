package com.chema.db.crudapi.exception;

// Import que contiene los codigos HTTP
import org.springframework.http.HttpStatus;
// Import que permite controlar status code, headers, body response
import org.springframework.http.ResponseEntity;
// Import para que esta clase maneje los errores
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.Map;

// Spring registra esta clase para que cuando haya un error recurra a ella
@RestControllerAdvice
public class GlobalExceptionHandler {

    // Este metodo se ejecuta cuando ocurre un TaskNotFoundException
    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleTaskNotFound(TaskNotFoundException exception) {
        Map<String, Object> errorResponse = Map.of(
                "status", HttpStatus.NOT_FOUND.value(),
                "message", exception.getMessage(),
                "error", "Not Found",
                "timestamp", LocalDateTime.now()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }
}
