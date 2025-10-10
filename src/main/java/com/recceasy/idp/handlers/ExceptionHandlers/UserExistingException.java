package com.recceasy.idp.handlers.ExceptionHandlers;

public class UserExistingException extends Exception{

    public UserExistingException(){
        super("User already exists");
    }
}
