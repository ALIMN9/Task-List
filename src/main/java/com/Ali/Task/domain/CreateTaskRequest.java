package com.Ali.Task.domain;
import com.Ali.Task.domain.entity.TaskPriority;

import java.time.LocalDate;

public record CreateTaskRequest(
        String title,
        String description,
        LocalDate dueDate,
        TaskPriority priority) {
}
