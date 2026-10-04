package com.Ali.Task.mapper;

import com.Ali.Task.domain.UpdateTaskRequest;
import com.Ali.Task.domain.dto.CreateTaskRequestDto;
import com.Ali.Task.domain.dto.TaskDto;
import com.Ali.Task.domain.CreateTaskRequest;
import com.Ali.Task.domain.dto.UpdateTaskRequestDto;
import com.Ali.Task.domain.entity.Task;

public interface TaskMapper{

    CreateTaskRequest FromDto (CreateTaskRequestDto dto);
    UpdateTaskRequest FromDto (UpdateTaskRequestDto dto);
    TaskDto ToDto (Task task);

}
