package com.recceasy.idp.handlers.ExceptionHandlers;

public class TokenExpiredException extends Exception{

    public TokenExpiredException(){
        super("Token Expired. Please try again.");
    }
}
