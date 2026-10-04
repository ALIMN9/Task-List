package com.Ali.Task.service;

import com.Ali.Task.domain.CreateTaskRequest;
import com.Ali.Task.domain.UpdateTaskRequest;
import com.Ali.Task.domain.entity.Task;

import java.util.List;
import java.util.UUID;

public interface TaskService {

    Task createTask(CreateTaskRequest request);

    List<Task> listTasks();

    Task updateTask(UUID id, UpdateTaskRequest updateTaskRequest);

    void deleteTask(UUID id);
}
