package com.Ali.Task.controller;

import com.Ali.Task.domain.dto.ErrorDto;
import com.Ali.Task.exception.TaskNotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.UUID;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDto> handelValidationExceptions(MethodArgumentNotValidException ex){

        String errorMessage = ex.getBindingResult().getFieldErrors().stream()
                .findFirst()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .orElse("Validation Failed.");

        ErrorDto errorDto = new ErrorDto(errorMessage);
        return new ResponseEntity<>(errorDto, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TaskNotFoundException.class)
    public ResponseEntity<ErrorDto> handelTaskNotFoundException(TaskNotFoundException ex){

        UUID taskNotFoundId = ex.getId();
        String errorMessage = String.format("Task with Id '%s' does not exist.",taskNotFoundId);
        ErrorDto errorDto = new  ErrorDto(errorMessage);
        return new  ResponseEntity<>(errorDto,HttpStatus.NOT_FOUND);
    }
}
