package com.chema.db.crudapi.controller;

import com.chema.db.crudapi.dto.TaskRequest;
import com.chema.db.crudapi.dto.TaskResponse;
import com.chema.db.crudapi.service.TaskService;

/*
Importa anotaciones REST de Spring.

Acá vienen:

@GetMapping
@PostMapping
@PutMapping
@DeleteMapping
@RequestBody
@PathVariable

etc.
 */
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.validation.Valid;

// Indica a Spring que esta clase maneja EndPoints combina @Controller y @ResponseBody
@RestController
// Define la URL del Controller
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        return taskService.getTaskById(id);
    }

    @PostMapping
    public TaskResponse createTask(@Valid @RequestBody TaskRequest request) {
        return taskService.createTask(request);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @Valid @RequestBody TaskRequest request) {

        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {
        taskService.deleteTask(id);
    }
}
