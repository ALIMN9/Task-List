package com.Ali.Task.domain;

import com.Ali.Task.domain.entity.TaskPriority;
import com.Ali.Task.domain.entity.TaskStatus;
import java.time.LocalDate;

public record UpdateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskStatus taskStatus,
        TaskPriority taskPriority
) {

}
