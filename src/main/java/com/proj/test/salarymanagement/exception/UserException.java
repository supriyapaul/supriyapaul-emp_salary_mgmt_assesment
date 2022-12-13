package com.proj.test.salarymanagement.exception;

public class UserException extends RuntimeException{
    private String message;

    public UserException(String message) {
        super(message);
        this.message = message;
    }
}
