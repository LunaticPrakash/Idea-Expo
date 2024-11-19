package com.ideaexpobackend.idea_expo_backend.exceptions;

import com.ideaexpobackend.idea_expo_backend.models.ErrorDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomBadCredentialsException.class)
    public ResponseEntity<ErrorDetails> handleBadCredentialException(CustomBadCredentialsException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), Arrays.toString(ex.getStackTrace()), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(CustomBadRequestException.class)
    public ResponseEntity<ErrorDetails> handleBadRequestException(CustomBadRequestException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), Arrays.toString(ex.getStackTrace()), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomRuntimeException.class)
    public ResponseEntity<ErrorDetails> handleCustomRuntimeException(CustomRuntimeException ex, WebRequest request) {
        ErrorDetails errorDetails = new ErrorDetails(new Date().toString(), ex.getMessage(), request.getDescription(true), Arrays.toString(ex.getStackTrace()), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(errorDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
