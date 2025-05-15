package com.example.FinalWorkDevelopmentOnSpringFramework.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleAllExceptions(RuntimeException ex) {
        return   ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}