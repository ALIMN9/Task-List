package com.Ali.Task.domain.dto;

import com.Ali.Task.domain.entity.TaskPriority;
import com.Ali.Task.domain.entity.TaskStatus;

import java.time.LocalDate;
import java.util.UUID;

public record TaskDto (
        UUID id,
        String title,
        String description,
        LocalDate dueDate,
        TaskPriority priority,
        TaskStatus status
){

}
