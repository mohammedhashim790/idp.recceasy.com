package com.recceasy.idp.handlers.ExceptionHandlers.password;

public class PasswordMissingUppercaseException extends RuntimeException {
    public PasswordMissingUppercaseException() {
        super("Password must include at least one uppercase letter.");
    }
}
