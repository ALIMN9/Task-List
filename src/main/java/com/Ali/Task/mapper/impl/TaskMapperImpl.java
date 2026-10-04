package com.Ali.Task.mapper.impl;

import com.Ali.Task.domain.UpdateTaskRequest;
import com.Ali.Task.domain.dto.CreateTaskRequestDto;
import com.Ali.Task.domain.dto.TaskDto;
import com.Ali.Task.domain.CreateTaskRequest;
import com.Ali.Task.domain.dto.UpdateTaskRequestDto;
import com.Ali.Task.domain.entity.Task;
import com.Ali.Task.mapper.TaskMapper;
import org.springframework.stereotype.Component;

@Component
public class TaskMapperImpl implements TaskMapper {

    @Override
    public CreateTaskRequest FromDto(CreateTaskRequestDto dto) {
        return new CreateTaskRequest(
                dto.title(),
                dto.description(),
                dto.dueDate(),
                dto.priority()
        );
    }

    @Override
    public TaskDto ToDto(Task task) {
        return new TaskDto(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getDueDate(),
                task.getTaskPriority(),
                task.getTaskStatus()
        );
    }

    @Override
    public UpdateTaskRequest FromDto(UpdateTaskRequestDto dto) {
        return new UpdateTaskRequest(
                dto.title(),
                dto.description(),
                dto.dueDate(),
                dto.status(),
                dto.priority()
        );
    }
}
