package com.recceasy.idp.handlers.ExceptionHandlers.username;

public class InvalidEmailException extends RuntimeException {
    public InvalidEmailException() {
        super("Username must be a valid email address");
    }
    public InvalidEmailException(String message) {
        super(message);
    }
}
