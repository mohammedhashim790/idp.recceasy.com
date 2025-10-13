package com.recceasy.idp.handlers.ExceptionHandlers.password;

public class PasswordMissingSpecialCharacterException extends RuntimeException {
    public PasswordMissingSpecialCharacterException(String specials) {
        super(String.format("Password must include at least one special. eg. %s", specials));
    }
}
