package com.example.exception;

public class UsernameAlreadyExistsException extends Exception {
    
    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
