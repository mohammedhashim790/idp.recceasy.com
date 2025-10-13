package com.recceasy.idp.handlers.ExceptionHandlers;

public class InvalidTokenException extends Exception{

    public InvalidTokenException(){
        super("Invalid Token. Please try again.");
    }
}
