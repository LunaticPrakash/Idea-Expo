package com.ideaexpobackend.idea_expo_backend.exceptions;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), stackTrace, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(CustomBadRequestException ex, WebRequest request) {
        String stackTrace = Objects.equals(activeProfile, "dev") ? Arrays.toString(ex.getStackTrace()) : "Not Available in Non-DEV environment";
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), stackTrace, HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ErrorDetails> handleCustomRuntimeException(CustomRuntimeException ex, WebRequest request) {
        String stackTrace = Objects.equals(activeProfile, "dev") ? Arrays.toString(ex.getStackTrace()) : "Not Available in Non-DEV environment";
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), stackTrace, HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
