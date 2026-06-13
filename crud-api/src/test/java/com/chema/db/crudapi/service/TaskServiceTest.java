package com.chema.db.crudapi.service;

// DTOs utilizados para las peticiones y respuestas
import com.chema.db.crudapi.dto.TaskRequest;
import com.chema.db.crudapi.dto.TaskResponse;

// Excepción personalizada cuando no existe una tarea
import com.chema.db.crudapi.exception.TaskNotFoundException;

// Entidades y enum del proyecto
import com.chema.db.crudapi.model.Task;
import com.chema.db.crudapi.model.TaskStatus;

// Repositorio que vamos a simular con Mockito
import com.chema.db.crudapi.repository.TaskRepository;

// Anotación para indicar que un método es un test
import org.junit.jupiter.api.Test;

// Permite integrar Mockito con JUnit
import org.junit.jupiter.api.extension.ExtendWith;

// Mockito crea objetos falsos (mocks)
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

// Métodos de validación de JUnit
import static org.junit.jupiter.api.Assertions.*;

// Métodos auxiliares de Mockito
import static org.mockito.Mockito.*;

// Activa Mockito para esta clase de test
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {

    // Mock del repositorio
    // No se utilizará PostgreSQL real durante los tests
    @Mock
    private TaskRepository taskRepository;

    // Clase real que queremos probar
    // Mockito inyectará automáticamente el mock anterior
    @InjectMocks
    private TaskService taskService;

    // Comprueba que createTask() funciona correctamente
    @Test
    void shouldCreateTask() {

        // Simula la petición que enviaría un cliente
        TaskRequest request = new TaskRequest();
        request.setTitle("Learn tests");
        request.setDescription("JUnit and Mockito");
        request.setStatus(TaskStatus.PENDING);

        // Simula la entidad que devolvería la base de datos
        // después de guardar la tarea
        Task savedTask = new Task();
        savedTask.setId(1L);
        savedTask.setTitle(request.getTitle());
        savedTask.setDescription(request.getDescription());
        savedTask.setStatus(request.getStatus());

        // Cuando se llame a save(...)
        // devolverá savedTask en lugar de acceder a PostgreSQL
        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        // Ejecuta el método real del servicio
        TaskResponse response =
                taskService.createTask(request);

        // Comprueba que la respuesta existe
        assertNotNull(response);

        // Comprueba que los datos son correctos
        assertEquals(1L, response.getId());
        assertEquals("Learn tests", response.getTitle());
        assertEquals(TaskStatus.PENDING, response.getStatus());

        // Comprueba que save() se llamó exactamente una vez
        verify(taskRepository, times(1))
                .save(any(Task.class));
    }

    // Comprueba que getTaskById() devuelve una tarea existente
    @Test
    void shouldGetTaskById() {

        // Simula una tarea existente en la base de datos
        Task task = new Task();
        task.setId(1L);
        task.setTitle("Existing task");
        task.setStatus(TaskStatus.PENDING);

        // Si se busca el ID 1
        // devolver la tarea simulada
        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        // Ejecuta el método real
        TaskResponse response =
                taskService.getTaskById(1L);

        // Verifica los datos devueltos
        assertEquals(1L, response.getId());
        assertEquals("Existing task", response.getTitle());

        // Comprueba que findById() se llamó una vez
        verify(taskRepository, times(1))
                .findById(1L);
    }

    // Comprueba que se lanza la excepción correcta
    // cuando la tarea no existe
    @Test
    void shouldThrowExceptionWhenTaskNotFound() {

        // Simula que la base de datos no encuentra la tarea
        when(taskRepository.findById(99L))
                .thenReturn(Optional.empty());

        // Espera que se lance TaskNotFoundException
        assertThrows(TaskNotFoundException.class,
                () -> taskService.getTaskById(99L));

        // Verifica que se realizó la búsqueda
        verify(taskRepository, times(1))
                .findById(99L);
    }
}