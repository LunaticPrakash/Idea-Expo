package com.ideaexpobackend.idea_expo_backend.exceptions;

public class CustomBadCredentialsException extends RuntimeException{

    public CustomBadCredentialsException(String message) {
        super(message);
    }
}
