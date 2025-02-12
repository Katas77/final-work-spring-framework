package com.example.FinalWorkDevelopmentOnSpringFramework.exception;

public class BusinessLogicException extends Throwable {
    public BusinessLogicException() {
    }
    public BusinessLogicException(Exception cause) {
        super(cause);
    }
    public BusinessLogicException(String message) {
        super(message);
    }
    public BusinessLogicException(String message, Exception cause) {
        super(message, cause);
    }



}
