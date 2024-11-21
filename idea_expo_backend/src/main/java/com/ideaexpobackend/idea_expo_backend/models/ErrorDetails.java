package com.ideaexpobackend.idea_expo_backend.models;

import java.util.Date;

public class ErrorDetails {

    private String timestamp;
    private String message;
    private String description;
    private String stackTrace;
    private int statusCode;

    public ErrorDetails() {
    }

    public ErrorDetails(String timestamp, String message, String description, String stackTrace, int statusCode) {
        this.timestamp = timestamp;
        this.message = message;
        this.description = description;
        this.stackTrace = stackTrace;
        this.statusCode = statusCode;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
