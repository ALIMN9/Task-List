package com.Ali.Task.controller;

import com.Ali.Task.domain.UpdateTaskRequest;
import com.Ali.Task.domain.dto.CreateTaskRequestDto;
import com.Ali.Task.domain.dto.TaskDto;
import com.Ali.Task.domain.CreateTaskRequest;
import com.Ali.Task.domain.dto.UpdateTaskRequestDto;
import com.Ali.Task.domain.entity.Task;
import com.Ali.Task.mapper.TaskMapper;
import com.Ali.Task.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/tasks")
public class TaskController {

    private  final TaskService taskService;
    private final TaskMapper taskMapper;

    public TaskController(TaskService taskService, TaskMapper taskMapper) {
        this.taskService = taskService;
        this.taskMapper = taskMapper;
    }

    @PostMapping
    public ResponseEntity<TaskDto> createTask(@Valid @RequestBody CreateTaskRequestDto createTaskRequestDto){
        CreateTaskRequest taskRequest = taskMapper.FromDto(createTaskRequestDto);
        Task task = taskService.createTask(taskRequest);
        return new ResponseEntity<>(taskMapper.ToDto(task), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<TaskDto>> ListTasks(){
        List<Task> tasksList = taskService.listTasks();
        List<TaskDto> taskDtoList = tasksList.stream().map(taskMapper::ToDto).toList();
        return ResponseEntity.ok(taskDtoList);
    }

    @PutMapping(path = "/{taskId}")
    public ResponseEntity<TaskDto> UpdateTask(@Valid @PathVariable("taskId") UUID taskId ,
                                              @RequestBody UpdateTaskRequestDto updateTaskRequestDto){

        UpdateTaskRequest taskRequest = taskMapper.FromDto(updateTaskRequestDto);
        Task updatedTask = taskService.updateTask(taskId, taskRequest);
        return new ResponseEntity<>(taskMapper.ToDto(updatedTask),HttpStatus.OK);
    }

    @DeleteMapping(path = "/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable("taskId") UUID id){
        taskService.deleteTask(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
