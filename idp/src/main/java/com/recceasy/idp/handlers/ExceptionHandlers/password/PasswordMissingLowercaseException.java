package com.recceasy.idp.handlers.ExceptionHandlers.password;

public class PasswordMissingLowercaseException extends RuntimeException {
    public PasswordMissingLowercaseException() {
        super("Password must include at least one lowercase letter.");
    }
}
