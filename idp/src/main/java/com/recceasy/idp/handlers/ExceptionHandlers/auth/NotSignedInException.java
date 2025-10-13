package com.recceasy.idp.handlers.ExceptionHandlers.auth;

public class NotSignedInException extends RuntimeException {
    public NotSignedInException() {
        super("User not signed in.");
    }
}
