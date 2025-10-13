package com.recceasy.idp.handlers.ExceptionHandlers;

public class DomainAlreadyExistsException extends Exception {

    public DomainAlreadyExistsException() {
        super("Domain not supported. Please try again.");
    }
}
