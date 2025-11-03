package com.example.FinalWorkDevelopmentOnSpringFramework.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}