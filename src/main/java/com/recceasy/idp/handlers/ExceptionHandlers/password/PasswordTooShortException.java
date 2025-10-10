package com.recceasy.idp.handlers.ExceptionHandlers.password;

public class PasswordTooShortException extends RuntimeException {
    public PasswordTooShortException() {
        super("Password must be at least 12 characters long.");
    }
}
