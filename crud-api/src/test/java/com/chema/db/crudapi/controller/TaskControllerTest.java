package com.chema.db.crudapi.controller;

// DTOs utilizados por el controlador
import com.chema.db.crudapi.dto.TaskRequest;
import com.chema.db.crudapi.dto.TaskResponse;

// Enum de estados de tarea
import com.chema.db.crudapi.model.TaskStatus;

// Servicio que será simulado con Mockito
import com.chema.db.crudapi.service.TaskService;

// Convierte objetos Java a JSON y viceversa
import com.fasterxml.jackson.databind.ObjectMapper;

// JUnit
import org.junit.jupiter.api.Test;

// Arranca únicamente la capa web (Controllers)
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;

// Sustituye un Bean de Spring por un mock
import org.springframework.test.context.bean.override.mockito.MockitoBean;

// Tipos MIME
import org.springframework.http.MediaType;

// Permite simular peticiones HTTP
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

// Mockito
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

// Validaciones de MockMvc
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

// Carga únicamente TaskController
// No arranca PostgreSQL ni la aplicación completa
@WebMvcTest(TaskController.class)
class TaskControllerTest {

    // Cliente HTTP falso para ejecutar GET, POST, PUT, DELETE...
    @Autowired
    private MockMvc mockMvc;

    // Convierte objetos Java a JSON
    @Autowired
    private ObjectMapper objectMapper;

    // Mock del servicio
    // El controlador lo utilizará en lugar del real
    @MockitoBean
    private TaskService taskService;

    // Comprueba GET /api/tasks
    @Test
    void shouldGetAllTasks() throws Exception {

        // Simula una tarea devuelta por el servicio
        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setTitle("Learn MockMvc");
        response.setDescription("Controller test");
        response.setStatus(TaskStatus.PENDING);

        // Cuando el controlador llame al servicio
        // devolver esta lista
        when(taskService.getAllTasks())
                .thenReturn(List.of(response));

        // Simula:
        // GET /api/tasks
        mockMvc.perform(get("/api/tasks"))

                // Espera HTTP 200
                .andExpect(status().isOk())

                // Espera:
                // [
                //   {
                //      "id":1
                //   }
                // ]
                .andExpect(jsonPath("$[0].id").value(1))

                // Comprueba el título
                .andExpect(
                        jsonPath("$[0].title")
                                .value("Learn MockMvc")
                )

                // Comprueba el estado
                .andExpect(
                        jsonPath("$[0].status")
                                .value("PENDING")
                );
    }

    // Comprueba POST /api/tasks
    @Test
    void shouldCreateTask() throws Exception {

        // Simula el JSON recibido del cliente
        TaskRequest request = new TaskRequest();
        request.setTitle("Create task");
        request.setDescription("Testing POST");
        request.setStatus(TaskStatus.PENDING);

        // Simula la respuesta del servicio
        TaskResponse response = new TaskResponse();
        response.setId(1L);
        response.setTitle("Create task");
        response.setDescription("Testing POST");
        response.setStatus(TaskStatus.PENDING);

        // Cuando se llame a createTask(...)
        // devolver la respuesta simulada
        when(taskService.createTask(any(TaskRequest.class)))
                .thenReturn(response);

        // Simula:
        // POST /api/tasks
        mockMvc.perform(

                        post("/api/tasks")

                                // Content-Type: application/json
                                .contentType(MediaType.APPLICATION_JSON)

                                // Convierte request a JSON
                                .content(
                                        objectMapper
                                                .writeValueAsString(request)
                                )
                )

                // Espera HTTP 200
                .andExpect(status().isOk())

                // Comprueba JSON devuelto
                .andExpect(jsonPath("$.id").value(1))

                .andExpect(
                        jsonPath("$.title")
                                .value("Create task")
                );
    }
}