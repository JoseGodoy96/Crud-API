package com.chema.db.crudapi.service;

import com.chema.db.crudapi.dto.TaskRequest;
import com.chema.db.crudapi.dto.TaskResponse;
import com.chema.db.crudapi.model.Task;

public class TaskMapper {

    public static Task toEntity(TaskRequest request) {
        Task task = new Task();

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());

        return task;
    }

    public static TaskResponse toResponse(Task task) {
        TaskResponse response = new TaskResponse();

        response.setId(task.getId());
        response.setTitle(task.getTitle());
        response.setDescription(task.getDescription());
        response.setStatus(task.getStatus());
        response.setCreatedAt(task.getCreatedAt());
        response.setUpdatedAt(task.getUpdatedAt());

        return response;
    }
}