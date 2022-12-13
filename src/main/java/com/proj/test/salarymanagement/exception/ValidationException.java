package com.proj.test.salarymanagement.exception;

public class ValidationException extends RuntimeException{
    private String message;

    public ValidationException(String message) {
        super(message);
        this.message = message;
    }
}
