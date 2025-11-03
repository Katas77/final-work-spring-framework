package com.example.FinalWorkDevelopmentOnSpringFramework.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDenied(AuthorizationDeniedException ex) {
        log.warn("Access denied: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.FORBIDDEN, ex);
    }

    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorResponse> handleBusinessLogic(BusinessLogicException ex) {
        log.warn("BusinessLogicException: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatus(ResponseStatusException ex) {
        log.warn("ResponseStatusException: reason={} message={}",
               ex.getReason(), ex.getMessage());
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorResponse> handleBadRequest(BadRequestException ex) {
        log.warn("BadRequestException: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException ex) {
        log.info("NotFoundException: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, ex);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExists(UserAlreadyExistsException ex) {
        log.warn("UserAlreadyExistsException: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, ex);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("IllegalArgumentException: {}", ex.getMessage());
        return buildResponse(HttpStatus.BAD_REQUEST, ex);
    }

    // Общий обработчик для неожиданных ошибок
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleAll(Exception ex) {
        log.error("Unexpected error", ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex);
    }

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, Exception ex) {
        ErrorResponse body = new ErrorResponse(
                LocalDateTime.now().toString(),
                status.value(),
                status.getReasonPhrase(),
                ex.getMessage() == null ? "Unexpected error" : ex.getMessage()
        );
        return ResponseEntity.status(status).body(body);
    }


    public static class ErrorResponse {
        private final String timestamp;
        private final int status;
        private final String error;
        private final String message;

        public ErrorResponse(String timestamp, int status, String error, String message) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
        }

        public String getTimestamp() { return timestamp; }
        public int getStatus() { return status; }
        public String getError() { return error; }
        public String getMessage() { return message; }
    }
}
