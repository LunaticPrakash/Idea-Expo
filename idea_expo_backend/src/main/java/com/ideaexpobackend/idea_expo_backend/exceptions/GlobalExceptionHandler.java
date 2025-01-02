package com.ideaexpobackend.idea_expo_backend.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @ExceptionHandler(CustomBadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialException(CustomBadCredentialsException ex, WebRequest request) {
        String stackTrace = Objects.equals(activeProfile, "dev") ? Arrays.toString(ex.getStackTrace()) : "Not Available in Non-DEV environment";
        HttpStatus statusCode = HttpStatus.UNAUTHORIZED;
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), stackTrace, statusCode.value());
        return new ResponseEntity<>(errorDetails, statusCode);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(CustomBadRequestException ex, WebRequest request) {
        String stackTrace = Objects.equals(activeProfile, "dev") ? Arrays.toString(ex.getStackTrace()) : "Not Available in Non-DEV environment";
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), stackTrace, statusCode.value());
        return new ResponseEntity<>(errorDetails, statusCode);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorDetails> handleCustomRuntimeException(RuntimeException ex, WebRequest request) {
        String stackTrace = Objects.equals(activeProfile, "dev") ? Arrays.toString(ex.getStackTrace()) : "Not Available in Non-DEV environment";
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), stackTrace, statusCode.value());
        return new ResponseEntity<>(errorDetails, statusCode);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorDetails> handleAccessDeniedException(AccessDeniedException ex, WebRequest request) {
        String stackTrace = Objects.equals(activeProfile, "dev") ? Arrays.toString(ex.getStackTrace()) : "Not Available in Non-DEV environment";
        HttpStatus statusCode = HttpStatus.FORBIDDEN;
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(false), stackTrace, statusCode.value());
        return new ResponseEntity<>(errorDetails, statusCode);
    }
}
