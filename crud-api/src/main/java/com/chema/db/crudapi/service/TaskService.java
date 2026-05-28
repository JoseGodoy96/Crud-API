package com.chema.db.crudapi.service;

import com.chema.db.crudapi.model.Task;
import com.chema.db.crudapi.repository.TaskRepository;
import com.chema.db.crudapi.exception.TaskNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

// Esta clase es un service Beam
@Service

public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    public Task getTaskById(Long id) {
        return taskRepository.findById(id)
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public Task createTask(Task task) {
        return taskRepository.save(task);
    }

    public Task updateTask(Long id, Task updatedTask) {
        return taskRepository.findById(id)
                .map(task -> {
                    task.setTitle(updatedTask.getTitle());
                    task.setDescription(updatedTask.getDescription());

                    if (updatedTask.getStatus() != null) {
                        task.setStatus(updatedTask.getStatus());
                    }

                    return taskRepository.save(task);
                })
                .orElseThrow(() -> new TaskNotFoundException(id));
    }

    public void deleteTask(Long id) {
        taskRepository.deleteById(id);
    }
}
