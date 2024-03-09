package com.assignment.projectmanagementsystem.exception;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private LocalDateTime tmeStamp;
    private String message;

    private List<String> fieldErrors = new ArrayList<>();

    public ErrorResponse() {
    }

    public ErrorResponse(LocalDateTime tmeStamp, String message, List<String> fieldErrors) {
        this.tmeStamp = tmeStamp;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public LocalDateTime getTmeStamp() {
        return tmeStamp;
    }

    public void setTmeStamp(LocalDateTime tmeStamp) {
        this.tmeStamp = tmeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<String> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
