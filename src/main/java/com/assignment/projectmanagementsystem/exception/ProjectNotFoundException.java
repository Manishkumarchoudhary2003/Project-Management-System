package com.assignment.projectmanagementsystem.exception;

public class ProjectNotFoundException extends Exception{

    public ProjectNotFoundException(String message){
        super(message);
    }
    public ProjectNotFoundException(String message, Throwable throwable){
        super(message, throwable);
    }
}
