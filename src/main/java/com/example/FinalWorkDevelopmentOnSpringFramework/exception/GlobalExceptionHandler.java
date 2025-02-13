package com.example.FinalWorkDevelopmentOnSpringFramework.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handleAllExceptions(Exception ex) {
        ModelAndView model = new ModelAndView("error");
        model.addObject("exception", ex.getMessage());
        return model;
    }
}