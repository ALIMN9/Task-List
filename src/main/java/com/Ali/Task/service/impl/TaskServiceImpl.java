package com.Ali.Task.service.impl;

import com.Ali.Task.domain.CreateTaskRequest;
import com.Ali.Task.domain.UpdateTaskRequest;
import com.Ali.Task.domain.entity.Task;
import com.Ali.Task.domain.entity.TaskStatus;
import com.Ali.Task.exception.TaskNotFoundException;
import com.Ali.Task.repository.TaskRepository;
import com.Ali.Task.service.TaskService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task createTask(CreateTaskRequest request) {
        Instant now = Instant.now();
        Task task = new Task(
                null,
                request.title(),
                request.description(),
                request.dueDate(),
                TaskStatus.OPEN,
                request.priority(),
                now,
                now
        );
        return taskRepository.save(task);
    }

    @Override
    public List<Task> listTasks() {
        return taskRepository.findAll(Sort.by(Sort.Direction.ASC,"created"));
    }

    @Override
    public Task updateTask(UUID TaskId, UpdateTaskRequest updateTaskRequest) {

        Task task = taskRepository.findById(TaskId).
                orElseThrow(() -> new TaskNotFoundException(TaskId));

        task.setTitle(updateTaskRequest.title());
        task.setDescription(updateTaskRequest.description());
        task.setDueDate(updateTaskRequest.dueDate());
        task.setTaskStatus(updateTaskRequest.taskStatus());
        task.setTaskPriority(updateTaskRequest.taskPriority());

        return taskRepository.save(task);
    }

    @Override
    public void deleteTask(UUID id) {
        taskRepository.deleteById(id);
    }
}
