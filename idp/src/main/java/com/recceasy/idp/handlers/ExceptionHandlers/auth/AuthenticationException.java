package com.recceasy.idp.handlers.ExceptionHandlers.auth;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String message) { super(message); }
}
